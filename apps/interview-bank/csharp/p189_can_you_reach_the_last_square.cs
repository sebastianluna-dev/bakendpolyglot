// 189 · Can You Reach the Last Square?
Check(CanReachLast([2, 0, 3, 0, 0, 1]));
Check(!CanReachLast([3, 1, 1, 0, 4]));
Check(CanReachLast([0]));
Check(!CanReachLast([0, 1]));
Check(CanReachLast([4, 0, 0, 0, 0]));
Check(!CanReachLast([1, 1, 0, 1]));
Check(CanReachLast([1, 2, 0, 0]));
Console.WriteLine("189 ok");

static bool CanReachLast(int[] jumps)
{
    var furthest = 0;
    for (var i = 0; i < jumps.Length; i++)
    {
        if (i > furthest) return false; // no reachable square jumps this far
        furthest = Math.Max(furthest, i + jumps[i]);
    }
    return true;
}

static void Check(bool ok)
{
    if (!ok) throw new Exception("test failed");
}
