// 163 · Ways to Climb the Stairs
Check(WaysToClimb(4) == 5);
Check(WaysToClimb(1) == 1);
Check(WaysToClimb(0) == 1);
Check(WaysToClimb(2) == 2);
Check(WaysToClimb(10) == 89);
Check(WaysToClimb(46) == 2971215073);
Check(WaysToClimb(70) == 308061521170129);
Console.WriteLine("163 ok");

static long WaysToClimb(int n)
{
    long prev = 1, cur = 1; // ways to reach steps i - 2 and i - 1
    for (var i = 2; i <= n; i++)
    {
        (prev, cur) = (cur, prev + cur);
    }
    return cur;
}

static void Check(bool ok)
{
    if (!ok) throw new Exception("test failed");
}
