// 097 · First and Last Position of a Value
int[] due = [3, 5, 5, 5, 8, 9];
Check(FirstAndLast(due, 5).SequenceEqual([1, 3]));
Check(FirstAndLast([3, 5, 8], 6).SequenceEqual([-1, -1]));
Check(FirstAndLast([], 1).SequenceEqual([-1, -1]));
Check(FirstAndLast([7, 7, 7, 7], 7).SequenceEqual([0, 3]));
Check(FirstAndLast([1], 1).SequenceEqual([0, 0]));
Check(FirstAndLast([1, 2, 2], 2).SequenceEqual([1, 2]));
Check(FirstAndLast(due, 1).SequenceEqual([-1, -1]));
Check(FirstAndLast(due, 10).SequenceEqual([-1, -1]));
Check(FirstAndLast(due, 3).SequenceEqual([0, 0]));
Check(FirstAndLast(due, 9).SequenceEqual([5, 5]));
Console.WriteLine("097 ok");

static int[] FirstAndLast(int[] days, int day)
{
    var first = FirstAtLeast(days, day);
    if (first == days.Length || days[first] != day) return [-1, -1];
    return [first, FirstAtLeast(days, day + 1) - 1]; // one before the first value >= day + 1
}

static int FirstAtLeast(int[] a, int target)
{
    int lo = 0, hi = a.Length;
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
