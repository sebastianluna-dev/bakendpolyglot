// 186 · Insert a New Interval
Check(SamePairs(InsertInterval([[1, 3], [6, 8], [10, 12], [15, 18]], [7, 10]), [[1, 3], [6, 12], [15, 18]]));
Check(SamePairs(InsertInterval([[2, 4], [9, 11]], [5, 7]), [[2, 4], [5, 7], [9, 11]]));
Check(SamePairs(InsertInterval([], [4, 6]), [[4, 6]]));
Check(SamePairs(InsertInterval([[5, 6]], [1, 2]), [[1, 2], [5, 6]]));
Check(SamePairs(InsertInterval([[1, 2]], [7, 9]), [[1, 2], [7, 9]]));
Check(SamePairs(InsertInterval([[3, 4], [6, 7]], [1, 10]), [[1, 10]]));
Check(SamePairs(InsertInterval([[1, 3]], [3, 5]), [[1, 5]]));
Check(SamePairs(InsertInterval([[5, 8]], [2, 5]), [[2, 8]]));
Console.WriteLine("186 ok");

static int[][] InsertInterval(int[][] intervals, int[] newInterval)
{
    var result = new List<int[]>();
    int start = newInterval[0], end = newInterval[1];
    var i = 0;
    while (i < intervals.Length && intervals[i][1] < start) result.Add(intervals[i++]);
    while (i < intervals.Length && intervals[i][0] <= end)
    {
        start = Math.Min(start, intervals[i][0]);
        end = Math.Max(end, intervals[i][1]);
        i++;
    }
    result.Add([start, end]);
    while (i < intervals.Length) result.Add(intervals[i++]);
    return result.ToArray();
}

static bool SamePairs(int[][] a, int[][] b) =>
    a.Length == b.Length && a.Zip(b).All(p => p.First.SequenceEqual(p.Second));

static void Check(bool ok)
{
    if (!ok) throw new Exception("test failed");
}
