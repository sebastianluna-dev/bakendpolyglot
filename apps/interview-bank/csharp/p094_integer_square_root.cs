// 094 · Integer Square Root
Check(IntegerSqrt(17) == 4);
Check(IntegerSqrt(36) == 6);
Check(IntegerSqrt(0) == 0);
Check(IntegerSqrt(1) == 1);
Check(IntegerSqrt(2) == 1);
Check(IntegerSqrt(4) == 2);
Check(IntegerSqrt(2_147_395_599) == 46_339);
Check(IntegerSqrt(2_147_395_600) == 46_340);
Check(IntegerSqrt(int.MaxValue) == 46_340);
Console.WriteLine("094 ok");

static int IntegerSqrt(int x)
{
    // long, because mid * mid and x + 1 overflow int near int.MaxValue
    long lo = 0, hi = (long)x + 1; // looking for the first k with k * k > x
    while (lo < hi)
    {
        var mid = lo + (hi - lo) / 2;
        if (mid * mid > x) hi = mid;
        else lo = mid + 1;
    }
    return (int)(lo - 1);
}

static void Check(bool ok)
{
    if (!ok) throw new Exception("test failed");
}
