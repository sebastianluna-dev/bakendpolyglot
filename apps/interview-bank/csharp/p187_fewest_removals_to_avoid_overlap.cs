// 187 · Fewest Removals to Avoid Overlap
Check(FewestRemovals([[1, 10], [2, 3], [4, 5], [6, 7]]) == 1);
Check(FewestRemovals([[1, 3], [3, 5], [5, 8]]) == 0);
Check(FewestRemovals([]) == 0);
Check(FewestRemovals([[1, 2], [1, 2], [1, 2]]) == 2);
Check(FewestRemovals([[1, 4], [2, 3], [3, 6], [5, 7]]) == 2);
Console.WriteLine("187 ok");

static int FewestRemovals(int[][] intervals)
{
    var byEnd = (int[][])intervals.Clone();
    Array.Sort(byEnd, (a, b) => a[1].CompareTo(b[1]));
    var removals = 0;
    var lastEnd = int.MinValue; // end of the last interval kept
    foreach (var iv in byEnd)
    {
        if (iv[0] >= lastEnd) lastEnd = iv[1];
        else removals++;
    }
    return removals;
}

static void Check(bool ok)
{
    if (!ok) throw new Exception("test failed");
}
