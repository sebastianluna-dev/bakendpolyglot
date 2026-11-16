// 165 · Best Stretch of Daily Results
Check(BestStretch([-2, 4, -1, 3, -7, 2]) == 6);
Check(BestStretch([-8, -3, -5]) == -3);
Check(BestStretch([5]) == 5);
Check(BestStretch([1, 2, 3]) == 6);
Check(BestStretch([-1, 0, -2]) == 0);
Check(BestStretch([2, -1, 2, -9, 4]) == 4);
Check(BestStretch([3, -2, 5, -1]) == 6);
Console.WriteLine("165 ok");

static int BestStretch(int[] results)
{
    int endingHere = 0, best = results[0]; // endingHere: best stretch that ends on day r
    foreach (var r in results)
    {
        endingHere = Math.Max(r, endingHere + r);
        best = Math.Max(best, endingHere);
    }
    return best;
}

static void Check(bool ok)
{
    if (!ok) throw new Exception("test failed");
}
