// 154 · All Subsets
Check(Same(AllSubsets([1, 2, 3]), [[], [1], [1, 2], [1, 2, 3], [1, 3], [2], [2, 3], [3]]));
Check(Same(AllSubsets([4, 1]), [[], [4], [4, 1], [1]]));
Check(Same(AllSubsets([]), [[]]));
Check(Same(AllSubsets([7]), [[], [7]]));
Check(Same(AllSubsets([-1, 0, 2]), [[], [-1], [-1, 0], [-1, 0, 2], [-1, 2], [0], [0, 2], [2]]));
Check(AllSubsets([0, 1, 2, 3, 4, 5, 6, 7, 8, 9]).Count == 1024);
Console.WriteLine("154 ok");

static List<int[]> AllSubsets(int[] nums)
{
    var result = new List<int[]>();
    var path = new List<int>();
    void Walk(int start)
    {
        result.Add(path.ToArray()); // a copy: path keeps changing
        for (var i = start; i < nums.Length; i++)
        {
            path.Add(nums[i]);
            Walk(i + 1);
            path.RemoveAt(path.Count - 1);
        }
    }
    Walk(0);
    return result;
}

static bool Same(List<int[]> got, int[][] want) =>
    got.Count == want.Length && got.Zip(want).All(p => p.First.SequenceEqual(p.Second));

static void Check(bool ok)
{
    if (!ok) throw new Exception("test failed");
}
