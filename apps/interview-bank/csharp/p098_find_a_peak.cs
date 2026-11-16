// 098 · Find a Peak
int[][] routes =
[
    [120, 135, 150, 140, 160, 155],
    [10, 20, 30],
    [5],
    [3, 1],
    [1, 3],
    [5, 4, 3, 2, 1],
    [1, 3, 2, 4, 3, 5, 4],
    [1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 9],
];
foreach (var route in routes) Check(IsPeak(route, FindPeak(route)));
Console.WriteLine("098 ok");

static int FindPeak(int[] altitudes)
{
    int lo = 0, hi = altitudes.Length - 1; // the last index can be the answer, but has no right neighbour to test
    while (lo < hi)
    {
        var mid = lo + (hi - lo) / 2;
        // Going down after mid: there is a peak at mid or to its left.
        if (altitudes[mid] > altitudes[mid + 1]) hi = mid;
        else lo = mid + 1;
    }
    return lo;
}

static bool IsPeak(int[] a, int i) =>
    i >= 0 && i < a.Length && (i == 0 || a[i - 1] < a[i]) && (i == a.Length - 1 || a[i + 1] < a[i]);

static void Check(bool ok)
{
    if (!ok) throw new Exception("test failed");
}
