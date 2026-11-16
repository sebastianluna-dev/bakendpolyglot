// 148 · How Far With Bricks and Ladders
Check(FurthestPlatform([3, 8, 6, 10, 11, 15, 7], 4, 1) == 3);
Check(FurthestPlatform([10, 12, 13, 25, 26], 4, 1) == 4);
Check(FurthestPlatform([7], 0, 0) == 0);
Check(FurthestPlatform([9, 7, 7, 3], 0, 0) == 3);
Check(FurthestPlatform([1, 2], 0, 0) == 0);
Check(FurthestPlatform([1, 2, 3, 4], 0, 3) == 3);
Check(FurthestPlatform([1, 6, 2, 9, 4, 5], 13, 0) == 5);
Check(FurthestPlatform([1, 6, 2, 9, 4, 5], 12, 0) == 4);
Console.WriteLine("148 ok");

static int FurthestPlatform(int[] heights, int bricks, int ladders)
{
    var onLadders = new PriorityQueue<int, int>(); // the climbs ladders cover so far
    for (var i = 0; i < heights.Length - 1; i++)
    {
        var climb = heights[i + 1] - heights[i];
        if (climb <= 0) continue;
        onLadders.Enqueue(climb, climb);
        if (onLadders.Count > ladders) bricks -= onLadders.Dequeue(); // the smallest climb goes to bricks
        if (bricks < 0) return i;
    }
    return heights.Length - 1;
}

static void Check(bool ok)
{
    if (!ok) throw new Exception("test failed");
}
