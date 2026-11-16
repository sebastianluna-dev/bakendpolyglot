// 096 · Minimum of a Rotated Sorted Array
Check(RotatedMinimum([900, 1020, 1200, 60, 300, 480]) == 60);
Check(RotatedMinimum([60, 300, 480]) == 60);
Check(RotatedMinimum([7]) == 7);
Check(RotatedMinimum([2, 1]) == 1);
Check(RotatedMinimum([3, 4, 5, 1, 2]) == 1);
Check(RotatedMinimum([2, 3, 4, 5, 1]) == 1);
Check(RotatedMinimum([5, 1, 2, 3, 4]) == 1);
Console.WriteLine("096 ok");

static int RotatedMinimum(int[] times)
{
    var last = times[^1];
    int lo = 0, hi = times.Length;
    while (lo < hi)
    {
        var mid = lo + (hi - lo) / 2;
        // Values <= last belong to the run after the wrap, which starts at the minimum.
        if (times[mid] <= last) hi = mid;
        else lo = mid + 1;
    }
    return times[lo];
}

static void Check(bool ok)
{
    if (!ok) throw new Exception("test failed");
}
