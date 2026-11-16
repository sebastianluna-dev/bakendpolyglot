// 156 · Combinations That Sum to a Target
Check(Same(CombinationsToTarget([2, 3, 5], 8), [[2, 2, 2, 2], [2, 3, 3], [3, 5]]));
Check(Same(CombinationsToTarget([7, 3, 2], 7), [[2, 2, 3], [7]]));
Check(CombinationsToTarget([4, 6], 5).Count == 0);
Check(Same(CombinationsToTarget([1], 3), [[1, 1, 1]]));
Check(Same(CombinationsToTarget([9], 9), [[9]]));
Check(Same(CombinationsToTarget([5, 1], 6), [[1, 1, 1, 1, 1, 1], [1, 5]]));
Console.WriteLine("156 ok");

static List<int[]> CombinationsToTarget(int[] candidates, int target)
{
    int[] sizes = [.. candidates];
    Array.Sort(sizes);
    var result = new List<int[]>();
    var path = new List<int>();
    void Walk(int start, int remaining)
    {
        if (remaining == 0)
        {
            result.Add(path.ToArray());
            return;
        }
        for (var i = start; i < sizes.Length; i++)
        {
            if (sizes[i] > remaining) break; // sorted: every later size is too big as well
            path.Add(sizes[i]);
            Walk(i, remaining - sizes[i]); // i, not i + 1: the same size may be used again
            path.RemoveAt(path.Count - 1);
        }
    }
    Walk(0, target);
    return result;
}

static bool Same(List<int[]> got, int[][] want) =>
    got.Count == want.Length && got.Zip(want).All(p => p.First.SequenceEqual(p.Second));

static void Check(bool ok)
{
    if (!ok) throw new Exception("test failed");
}
