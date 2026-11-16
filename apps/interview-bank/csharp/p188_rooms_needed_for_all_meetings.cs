// 188 · Rooms Needed for All Meetings
Check(RoomsNeeded([[9, 12], [10, 11], [11, 13], [13, 14]]) == 2);
Check(RoomsNeeded([[8, 10], [10, 12], [12, 14]]) == 1);
Check(RoomsNeeded([]) == 0);
Check(RoomsNeeded([[1, 5], [2, 6], [3, 7]]) == 3);
Check(RoomsNeeded([[2, 3], [2, 3]]) == 2);
Check(RoomsNeeded([[5, 10], [1, 4], [4, 6]]) == 2);
Console.WriteLine("188 ok");

static int RoomsNeeded(int[][] meetings)
{
    var starts = meetings.Select(m => m[0]).ToArray();
    var ends = meetings.Select(m => m[1]).ToArray();
    Array.Sort(starts);
    Array.Sort(ends);
    int rooms = 0, j = 0; // ends[j] is the earliest end that has not freed a room yet
    foreach (var start in starts)
    {
        if (start >= ends[j]) j++; // a meeting is over: reuse its room
        else rooms++;
    }
    return rooms;
}

static void Check(bool ok)
{
    if (!ok) throw new Exception("test failed");
}
