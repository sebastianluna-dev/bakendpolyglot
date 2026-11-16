// 197 · Retry With Exponential Backoff and Jitter
var delays = new List<int>();
Check(Retry(Flaky(2, "receipt-42"), 5, 100, 1000, delays.Add, Cycle([0.5])) == "receipt-42");
Check(delays.SequenceEqual([50, 100]));

delays.Clear();
var op = Flaky(10, "never");
Check(Failure(() => Retry(op, 4, 100, 250, delays.Add, Cycle([0.5, 0.25, 0.75]))) == "timeout 4");
Check(delays.SequenceEqual([50, 50, 187]));

delays.Clear();
Check(Retry(Flaky(0, "first try"), 3, 100, 1000, delays.Add, Cycle([0.5])) == "first try");
Check(delays.Count == 0);

delays.Clear();
Check(Failure(() => Retry(Flaky(1, "late"), 1, 100, 1000, delays.Add, Cycle([0.5]))) == "timeout 1");
Check(delays.Count == 0);

delays.Clear();
Check(Retry(Flaky(6, "ok"), 7, 1000, 5000, delays.Add, Cycle([0.999])) == "ok");
Check(delays.SequenceEqual([999, 1998, 3996, 4995, 4995, 4995]));
Console.WriteLine("197 ok");

static T Retry<T>(Func<T> operation, int maxAttempts, int baseDelayMs, int capMs, Action<int> sleep, Func<double> random)
{
    var ceiling = baseDelayMs;
    for (var attempt = 1; attempt < maxAttempts; attempt++)
    {
        try
        {
            return operation();
        }
        catch (Exception)
        {
            sleep((int)(random() * ceiling)); // full jitter: anywhere in [0, ceiling)
            ceiling = Math.Min(capMs, ceiling * 2);
        }
    }
    return operation(); // the last attempt: its exception reaches the caller
}

static Func<string> Flaky(int failures, string result)
{
    var calls = 0;
    return () =>
    {
        calls++;
        if (calls <= failures) throw new TimeoutException($"timeout {calls}");
        return result;
    };
}

static Func<double> Cycle(double[] values)
{
    var calls = 0;
    return () => values[calls++ % values.Length];
}

static string Failure(Func<string> op)
{
    try
    {
        op();
        return "no error";
    }
    catch (TimeoutException e)
    {
        return e.Message;
    }
}

static void Check(bool ok)
{
    if (!ok) throw new Exception("test failed");
}
