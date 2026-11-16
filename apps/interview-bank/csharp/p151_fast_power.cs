// 151 · Fast Power
Check(PowerMod(3, 13, 100) == 23);
Check(PowerMod(7, 0, 13) == 1);
Check(PowerMod(2, 10, 1000) == 24);
Check(PowerMod(5, 3, 1) == 0);
Check(PowerMod(0, 0, 7) == 1);
Check(PowerMod(0, 5, 7) == 0);
Check(PowerMod(999999999, 2, 1000000007) == 64);
Check(PowerMod(10, 9, 1000000007) == 1000000000);
Check(PowerMod(2, 1000000006, 1000000007) == 1);
Console.WriteLine("151 ok");

static long PowerMod(long x, long n, long m)
{
    if (n == 0) return 1 % m;
    var half = PowerMod(x, n / 2, m); // one recursive call, used twice
    var square = half * half % m;
    return n % 2 == 0 ? square : square * (x % m) % m;
}

static void Check(bool ok)
{
    if (!ok) throw new Exception("test failed");
}
