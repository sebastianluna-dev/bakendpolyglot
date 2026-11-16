// 171 · Longest Rising Sequence
Check(LongestRising([3, 1, 8, 2, 5, 9, 4]) == 4);
Check(LongestRising([7, 7, 7]) == 1);
Check(LongestRising([]) == 0);
Check(LongestRising([5]) == 1);
Check(LongestRising([9, 8, 7]) == 1);
Check(LongestRising([1, 2, 3, 4]) == 4);
Check(LongestRising([2, 2, 3, 3, 4]) == 3);
Check(LongestRising([6, 2, 9, 3, 7, 4, 10, 5, 8]) == 5);
Check(LongestRising([10, -3, 4, -1, 6, 2, 8]) == 4);
Console.WriteLine("171 ok");

static int LongestRising(int[] nums)
{
    var tails = new List<int>(); // tails[k] = smallest last value of a rising sequence of length k + 1
    foreach (var x in nums)
    {
        int lo = 0, hi = tails.Count;
        while (lo < hi)
        {
            // first tail >= x, so an equal value replaces instead of extending
            var mid = (lo + hi) / 2;
            if (tails[mid] < x) lo = mid + 1;
            else hi = mid;
        }
        if (lo == tails.Count) tails.Add(x);
        else tails[lo] = x;
    }
    return tails.Count;
}

static void Check(bool ok)
{
    if (!ok) throw new Exception("test failed");
}
