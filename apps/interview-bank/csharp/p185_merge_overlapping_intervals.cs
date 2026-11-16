// 185 · Merge Overlapping Intervals
Check(SamePairs(MergeIntervals([[9, 12], [1, 3], [2, 6], [8, 9]]), [[1, 6], [8, 12]]));
Check(SamePairs(MergeIntervals([[1, 10], [2, 3], [4, 5]]), [[1, 10]]));
Check(SamePairs(MergeIntervals([]), []));
Check(SamePairs(MergeIntervals([[5, 5]]), [[5, 5]]));
Check(SamePairs(MergeIntervals([[3, 4], [1, 2]]), [[1, 2], [3, 4]]));
Check(SamePairs(MergeIntervals([[4, 6], [1, 4], [6, 6]]), [[1, 6]]));
Console.WriteLine("185 ok");

static int[][] MergeIntervals(int[][] intervals)
{
    var merged = new List<int[]>();
    foreach (var iv in intervals.OrderBy(x => x[0]))
    {
        if (merged.Count > 0 && iv[0] <= merged[^1][1])
            merged[^1][1] = Math.Max(merged[^1][1], iv[1]);
        else
            merged.Add([iv[0], iv[1]]); // a new pair, so extending it never changes the input
    }
    return merged.ToArray();
}

static bool SamePairs(int[][] a, int[][] b) =>
    a.Length == b.Length && a.Zip(b).All(p => p.First.SequenceEqual(p.Second));

static void Check(bool ok)
{
    if (!ok) throw new Exception("test failed");
}
