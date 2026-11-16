// 155 · All Orderings
Check(Same(AllOrderings([1, 2, 3]), [[1, 2, 3], [1, 3, 2], [2, 1, 3], [2, 3, 1], [3, 1, 2], [3, 2, 1]]));
Check(Same(AllOrderings([3, 1, 2]), [[3, 1, 2], [3, 2, 1], [1, 3, 2], [1, 2, 3], [2, 3, 1], [2, 1, 3]]));
Check(Same(AllOrderings([]), [[]]));
Check(Same(AllOrderings([9]), [[9]]));
Check(Same(AllOrderings([5, -5]), [[5, -5], [-5, 5]]));
Check(AllOrderings([1, 2, 3, 4, 5, 6]).Count == 720);
Console.WriteLine("155 ok");

static List<int[]> AllOrderings(int[] nums)
{
    var result = new List<int[]>();
    var path = new List<int>();
    var used = new bool[nums.Length];
    void Walk()
    {
        if (path.Count == nums.Length)
        {
            result.Add(path.ToArray());
            return;
        }
        for (var i = 0; i < nums.Length; i++)
        {
            if (used[i]) continue;
            used[i] = true;
            path.Add(nums[i]);
            Walk();
            path.RemoveAt(path.Count - 1);
            used[i] = false;
        }
    }
    Walk();
    return result;
}

static bool Same(List<int[]> got, int[][] want) =>
    got.Count == want.Length && got.Zip(want).All(p => p.First.SequenceEqual(p.Second));

static void Check(bool ok)
{
    if (!ok) throw new Exception("test failed");
}
