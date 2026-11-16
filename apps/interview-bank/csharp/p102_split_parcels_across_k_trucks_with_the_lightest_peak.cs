// 102 · Split Parcels Across k Trucks With the Lightest Peak
Check(LightestPeak([4, 9, 3, 6, 8, 2], 3) == 13);
Check(LightestPeak([3, 1, 12, 2], 3) == 12);
Check(LightestPeak([4, 9, 3], 1) == 16);
Check(LightestPeak([2, 8, 5], 3) == 8);
Check(LightestPeak([7], 1) == 7);
Check(LightestPeak([1, 1, 1, 1, 1, 1], 4) == 2);
Console.WriteLine("102 ok");

static int LightestPeak(int[] weights, int k)
{
    int lo = weights.Max(), hi = weights.Sum();
    while (lo < hi)
    {
        var mid = lo + (hi - lo) / 2;
        if (TrucksNeeded(weights, mid) <= k) hi = mid;
        else lo = mid + 1;
    }
    return lo;
}

static int TrucksNeeded(int[] weights, int limit)
{
    int trucks = 1, load = 0;
    foreach (var w in weights)
    {
        if (load + w > limit)
        {
            trucks++;
            load = 0;
        }
        load += w;
    }
    return trucks;
}

static void Check(bool ok)
{
    if (!ok) throw new Exception("test failed");
}
