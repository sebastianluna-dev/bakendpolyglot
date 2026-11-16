// 093 · The First Failing Build
var calls = 0;
Func<int, bool> FailingFrom(int first) => build =>
{
    calls++;
    return build >= first;
};

Check(FirstFailingBuild(5, FailingFrom(4)) == 4);
Check(FirstFailingBuild(3, FailingFrom(4)) == -1);
Check(FirstFailingBuild(1, FailingFrom(1)) == 1);
Check(FirstFailingBuild(1, FailingFrom(2)) == -1);
Check(FirstFailingBuild(5, FailingFrom(1)) == 1);
calls = 0;
Check(FirstFailingBuild(1000, FailingFrom(777)) == 777);
Check(calls <= 10);
calls = 0;
Check(FirstFailingBuild(2_000_000_000, FailingFrom(1_999_999_999)) == 1_999_999_999);
Check(calls <= 31);
Console.WriteLine("093 ok");

static int FirstFailingBuild(int n, Func<int, bool> isBad)
{
    int lo = 1, hi = n + 1; // n + 1 stands for "no build fails"
    while (lo < hi)
    {
        var mid = lo + (hi - lo) / 2;
        if (isBad(mid)) hi = mid;
        else lo = mid + 1;
    }
    return lo <= n ? lo : -1;
}

static void Check(bool ok)
{
    if (!ok) throw new Exception("test failed");
}
