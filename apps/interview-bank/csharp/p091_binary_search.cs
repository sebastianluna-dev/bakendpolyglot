// 091 · Binary Search
int[] ids = [3, 8, 15, 21, 40, 57];
Check(BinarySearch(ids, 21) == 3);
Check(BinarySearch(ids, 10) == -1);
Check(BinarySearch([], 5) == -1);
Check(BinarySearch([7], 7) == 0);
Check(BinarySearch(ids, 3) == 0);
Check(BinarySearch(ids, 57) == 5);
Check(BinarySearch(ids, 1) == -1);
Check(BinarySearch(ids, 99) == -1);
Console.WriteLine("091 ok");

static int BinarySearch(int[] ids, int target)
{
    int lo = 0, hi = ids.Length; // the first ID >= target is somewhere in [lo, hi)
    while (lo < hi)
    {
        var mid = lo + (hi - lo) / 2;
        if (ids[mid] >= target) hi = mid;
        else lo = mid + 1;
    }
    return lo < ids.Length && ids[lo] == target ? lo : -1;
}

static void Check(bool ok)
{
    if (!ok) throw new Exception("test failed");
}
