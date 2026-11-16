// 092 · Where Would It Go?
int[] stops = [480, 540, 600, 720];
Check(InsertPosition(stops, 610) == 3);
Check(InsertPosition([480, 540, 540, 600], 540) == 1);
Check(InsertPosition([], 300) == 0);
Check(InsertPosition(stops, 100) == 0);
Check(InsertPosition(stops, 800) == 4);
Check(InsertPosition(stops, 480) == 0);
Check(InsertPosition(stops, 720) == 3);
Check(InsertPosition([5, 5, 5], 5) == 0);
Console.WriteLine("092 ok");

static int InsertPosition(int[] deadlines, int deadline)
{
    int lo = 0, hi = deadlines.Length; // the answer may be deadlines.Length itself
    while (lo < hi)
    {
        var mid = lo + (hi - lo) / 2;
        if (deadlines[mid] >= deadline) hi = mid;
        else lo = mid + 1;
    }
    return lo;
}

static void Check(bool ok)
{
    if (!ok) throw new Exception("test failed");
}
