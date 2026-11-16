// 199 · Bounded Worker Pool With Ordered Results
int[] durations = [40, 20, 30, 20, 25, 20, 30, 20]; // later tasks often finish before earlier ones
var (results, peak) = await Measure(durations, 3);
Check(results.SequenceEqual([400, 200, 300, 200, 250, 200, 300, 200]) && peak == 3);
(results, peak) = await Measure([20, 20, 20], 1);
Check(results.SequenceEqual([200, 200, 200]) && peak == 1);
(results, peak) = await Measure([30, 20], 10);
Check(results.SequenceEqual([300, 200]) && peak == 2);
(results, peak) = await Measure([], 4);
Check(results.Length == 0 && peak == 0);
Console.WriteLine("199 ok");

static async Task<R[]> MapLimit<T, R>(T[] items, int limit, Func<T, Task<R>> fn)
{
    var results = new R[items.Length];
    var next = -1;
    async Task Worker()
    {
        int i;
        while ((i = Interlocked.Increment(ref next)) < items.Length) // each index goes to one worker
            results[i] = await fn(items[i]);
    }
    var workers = Enumerable.Range(0, Math.Min(limit, items.Length)).Select(_ => Worker());
    await Task.WhenAll(workers);
    return results;
}

static async Task<(int[] Results, int Peak)> Measure(int[] durations, int limit)
{
    var gate = new Lock();
    var running = 0;
    var peak = 0;
    var results = await MapLimit(durations, limit, async ms =>
    {
        lock (gate)
        {
            running++;
            peak = Math.Max(peak, running);
        }
        await Task.Delay(ms);
        lock (gate) running--;
        return ms * 10;
    });
    return (results, peak);
}

static void Check(bool ok)
{
    if (!ok) throw new Exception("test failed");
}
