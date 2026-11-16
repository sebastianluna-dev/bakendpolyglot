// 181 · Can One Person Attend Every Meeting?
Check(CanAttendAll([[540, 600], [600, 660], [480, 540]]));
Check(!CanAttendAll([[540, 630], [600, 660]]));
Check(CanAttendAll([]));
Check(CanAttendAll([[100, 200]]));
Check(!CanAttendAll([[700, 800], [100, 200], [150, 160]]));
Check(!CanAttendAll([[5, 10], [5, 6]]));
Check(CanAttendAll([[0, 1], [2, 3], [1, 2]]));
Console.WriteLine("181 ok");

static bool CanAttendAll(int[][] meetings)
{
    Array.Sort(meetings, (a, b) => a[0].CompareTo(b[0]));
    for (var i = 1; i < meetings.Length; i++)
    {
        if (meetings[i][0] < meetings[i - 1][1]) return false; // starting just as the last one ends is fine
    }
    return true;
}

static void Check(bool ok)
{
    if (!ok) throw new Exception("test failed");
}
