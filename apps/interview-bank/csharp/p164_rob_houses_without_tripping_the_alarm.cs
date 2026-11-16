// 164 · Rob Houses Without Tripping the Alarm
Check(MaxHaul([6, 1, 2, 9]) == 15);
Check(MaxHaul([5, 10, 4, 3, 8]) == 18);
Check(MaxHaul([]) == 0);
Check(MaxHaul([7]) == 7);
Check(MaxHaul([2, 1, 1, 2]) == 4);
Check(MaxHaul([4, 1, 2, 7, 5, 3, 1]) == 14);
Check(MaxHaul([0, 0, 0]) == 0);
Console.WriteLine("164 ok");

static int MaxHaul(int[] cash)
{
    int skipPrev = 0, best = 0; // best totals up to two houses back and up to here
    foreach (var c in cash)
    {
        (skipPrev, best) = (best, Math.Max(best, skipPrev + c));
    }
    return best;
}

static void Check(bool ok)
{
    if (!ok) throw new Exception("test failed");
}
