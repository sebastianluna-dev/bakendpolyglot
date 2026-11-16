// 145 · Schedule Tasks With a Cooldown
Check(ScheduleTime("AAABBC", 2) == 7);
Check(ScheduleTime("AABBCD", 1) == 6);
Check(ScheduleTime("AAABBB", 2) == 8);
Check(ScheduleTime("AAAA", 3) == 13);
Check(ScheduleTime("ABAB", 0) == 4);
Check(ScheduleTime("A", 5) == 1);
Check(ScheduleTime("AAABCDEFG", 2) == 9);
Console.WriteLine("145 ok");

static int ScheduleTime(string tasks, int cooldown)
{
    var counts = new int[26];
    foreach (var t in tasks) counts[t - 'A']++;
    // copies left per type, most first
    var heap = new PriorityQueue<int, int>(Comparer<int>.Create((a, b) => b.CompareTo(a)));
    foreach (var c in counts)
    {
        if (c > 0) heap.Enqueue(c, c);
    }
    var waiting = new Queue<(int Left, int Ready)>(); // ready: the time it may run again
    var time = 0;
    while (heap.Count > 0 || waiting.Count > 0)
    {
        time++;
        if (heap.Count == 0)
        {
            time = waiting.Peek().Ready; // idle until the next type comes off cooldown
        }
        else
        {
            var left = heap.Dequeue() - 1;
            if (left > 0) waiting.Enqueue((left, time + cooldown));
        }
        if (waiting.Count > 0 && waiting.Peek().Ready == time)
        {
            var left = waiting.Dequeue().Left;
            heap.Enqueue(left, left);
        }
    }
    return time;
}

static void Check(bool ok)
{
    if (!ok) throw new Exception("test failed");
}
