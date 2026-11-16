// 175 · Split Into Two Equal-Sum Groups
Check(CanSplitEvenly([3, 1, 4, 2, 2]));
Check(!CanSplitEvenly([2, 3, 7]));
Check(!CanSplitEvenly([1]));
Check(CanSplitEvenly([5, 5]));
Check(!CanSplitEvenly([1, 5]));
Check(!CanSplitEvenly([1, 2, 4]));
Check(CanSplitEvenly([1, 2, 3, 4, 5, 6, 7]));
Check(!CanSplitEvenly([6, 6, 6, 6, 6, 4]));
Check(CanSplitEvenly(Enumerable.Repeat(100, 200).ToArray()));
Console.WriteLine("175 ok");

static bool CanSplitEvenly(int[] weights)
{
    var total = weights.Sum();
    if (total % 2 != 0) return false;
    var half = total / 2;
    var reachable = new bool[half + 1]; // reachable[s]: some parcels seen so far weigh s
    reachable[0] = true;
    foreach (var w in weights)
    {
        for (var s = half; s >= w; s--) // downwards, so this parcel is counted at most once
        {
            if (reachable[s - w]) reachable[s] = true;
        }
    }
    return reachable[half];
}

static void Check(bool ok)
{
    if (!ok) throw new Exception("test failed");
}
