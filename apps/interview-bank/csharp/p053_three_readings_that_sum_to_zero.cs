// 053 · Three Readings That Sum to Zero
Check(Same(ThreeToZero([-4, 1, 3, 0, -1, 2, -1]), [[-4, 1, 3], [-1, -1, 2], [-1, 0, 1]]));
Check(Same(ThreeToZero([0, 0, 0, 0]), [[0, 0, 0]]));
Check(ThreeToZero([1, 2, -4]).Count == 0);
Check(ThreeToZero([]).Count == 0);
Check(ThreeToZero([5, -5]).Count == 0);
Check(Same(ThreeToZero([2, -2, 0, 2, 0]), [[-2, 0, 2]]));
Console.WriteLine("053 ok");

static List<int[]> ThreeToZero(int[] readings)
{
    int[] s = [.. readings];
    Array.Sort(s);
    var result = new List<int[]>();
    for (var i = 0; i < s.Length - 2; i++)
    {
        if (i > 0 && s[i] == s[i - 1]) continue; // same first value, same triplets
        int lo = i + 1, hi = s.Length - 1;
        while (lo < hi)
        {
            var sum = s[i] + s[lo] + s[hi];
            if (sum < 0) lo++;
            else if (sum > 0) hi--;
            else
            {
                result.Add([s[i], s[lo], s[hi]]);
                lo++;
                hi--;
                while (lo < hi && s[lo] == s[lo - 1]) lo++;
            }
        }
    }
    return result;
}

static bool Same(List<int[]> got, int[][] want) =>
    got.Count == want.Length && got.Zip(want).All(p => p.First.SequenceEqual(p.Second));

static void Check(bool ok)
{
    if (!ok) throw new Exception("test failed");
}
