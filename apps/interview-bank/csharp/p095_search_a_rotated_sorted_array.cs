// 095 · Search a Rotated Sorted Array
int[] board = [900, 1020, 1200, 60, 300, 480];
Check(SearchRotated(board, 300) == 4);
Check(SearchRotated(board, 600) == -1);
Check(SearchRotated(board, 1020) == 1);
Check(SearchRotated(board, 900) == 0);
Check(SearchRotated(board, 480) == 5);
Check(SearchRotated(board, 60) == 3);
Check(SearchRotated(board, 1300) == -1);
Check(SearchRotated(board, 10) == -1);
Check(SearchRotated([1, 2, 3], 3) == 2);
Check(SearchRotated([5], 5) == 0);
Check(SearchRotated([5], 1) == -1);
Check(SearchRotated([], 1) == -1);
Check(SearchRotated([2, 1], 1) == 1);
Check(SearchRotated([2, 1], 2) == 0);
Console.WriteLine("095 ok");

static int SearchRotated(int[] times, int target)
{
    var n = times.Length;
    if (n == 0) return -1;
    var last = times[n - 1];
    int lo = 0, hi = n;
    while (lo < hi)
    {
        var mid = lo + (hi - lo) / 2;
        if (times[mid] <= last) hi = mid;
        else lo = mid + 1;
    }
    var start = lo; // index of the minimum: [start, n) and [0, start) are both sorted
    var (from, to) = target <= last ? (start, n) : (0, start);
    var i = FirstAtLeast(times, from, to, target);
    return i < to && times[i] == target ? i : -1;
}

static int FirstAtLeast(int[] a, int lo, int hi, int target)
{
    while (lo < hi)
    {
        var mid = lo + (hi - lo) / 2;
        if (a[mid] >= target) hi = mid;
        else lo = mid + 1;
    }
    return lo;
}

static void Check(bool ok)
{
    if (!ok) throw new Exception("test failed");
}
