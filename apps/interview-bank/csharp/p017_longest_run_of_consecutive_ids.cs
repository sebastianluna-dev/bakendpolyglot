// 017 · Longest Run of Consecutive IDs
Check(LongestRun([104, 7, 101, 5, 103, 6, 102]) == 4);
Check(LongestRun([9, 1, 9, 2, 2]) == 2);
Check(LongestRun([]) == 0);
Check(LongestRun([5]) == 1);
Check(LongestRun([-1, 0, 1, 3]) == 3);
Check(LongestRun([10, 30, 20]) == 1);
Console.WriteLine("017 ok");

static int LongestRun(int[] ids)
{
    var present = new HashSet<int>(ids);
    var best = 0;
    foreach (var id in present)
    {
        if (present.Contains(id - 1)) continue; // not the start of a run
        var length = 1;
        while (present.Contains(id + length)) length++;
        best = Math.Max(best, length);
    }
    return best;
}

static void Check(bool ok)
{
    if (!ok) throw new Exception("test failed");
}
