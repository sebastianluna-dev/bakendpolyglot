// 101 · Median of Two Sorted Arrays
Check(MedianOfTwoSorted([12, 18, 31], [9, 20, 25, 40]) == 20);
Check(MedianOfTwoSorted([5, 9], [6, 30]) == 7.5);
Check(MedianOfTwoSorted([], [4]) == 4);
Check(MedianOfTwoSorted([1, 2], []) == 1.5);
Check(MedianOfTwoSorted([2, 2, 2], [2, 2]) == 2);
Check(MedianOfTwoSorted([1, 2], [3, 4, 5, 6]) == 3.5);
Check(MedianOfTwoSorted([1, 3, 5, 7, 9], [2]) == 4);
Check(MedianOfTwoSorted([-5, -1], [-3]) == -3);
Check(MedianOfTwoSorted([10], [1, 2, 3]) == 2.5);
Console.WriteLine("101 ok");

static double MedianOfTwoSorted(int[] a, int[] b)
{
    if (a.Length > b.Length) return MedianOfTwoSorted(b, a);
    int m = a.Length, n = b.Length;
    var half = (m + n + 1) / 2;
    int lo = 0, hi = m; // m stands for "take all of a"
    while (lo < hi)
    {
        var mid = lo + (hi - lo) / 2; // values of a in the left half
        var rest = half - mid;
        if (rest == 0 || b[rest - 1] <= a[mid]) hi = mid;
        else lo = mid + 1;
    }
    var i = lo;
    var j = half - i;
    var leftMax = Math.Max(i > 0 ? a[i - 1] : int.MinValue, j > 0 ? b[j - 1] : int.MinValue);
    if ((m + n) % 2 == 1) return leftMax;
    var rightMin = Math.Min(i < m ? a[i] : int.MaxValue, j < n ? b[j] : int.MaxValue);
    return (leftMax + rightMin) / 2.0;
}

static void Check(bool ok)
{
    if (!ok) throw new Exception("test failed");
}
