// 065 · Maximum of Every Window
Check(WindowMaxima([3, 1, -2, 5, 4, 2, 6], 3).SequenceEqual([3, 5, 5, 5, 6]));
Check(WindowMaxima([2, 7, 1], 3).SequenceEqual([7]));
Check(WindowMaxima([4, -1, 4], 1).SequenceEqual([4, -1, 4]));
Check(WindowMaxima([], 2).Length == 0);
Check(WindowMaxima([1, 2], 3).Length == 0);
Check(WindowMaxima([5, 5, 1, 5], 2).SequenceEqual([5, 5, 5]));
Check(WindowMaxima([9, 7, 5, 3], 2).SequenceEqual([9, 7, 5]));
Check(WindowMaxima([9, 1, 1, 1], 2).SequenceEqual([9, 1, 1]));
Console.WriteLine("065 ok");

static int[] WindowMaxima(int[] readings, int k)
{
    var result = new List<int>();
    var window = new LinkedList<int>(); // indices; their readings decrease from First to Last
    for (var i = 0; i < readings.Length; i++)
    {
        while (window.Count > 0 && readings[window.Last!.Value] <= readings[i]) window.RemoveLast();
        window.AddLast(i);
        if (window.First!.Value <= i - k) window.RemoveFirst();
        if (i >= k - 1) result.Add(readings[window.First!.Value]);
    }
    return result.ToArray();
}

static void Check(bool ok)
{
    if (!ok) throw new Exception("test failed");
}
