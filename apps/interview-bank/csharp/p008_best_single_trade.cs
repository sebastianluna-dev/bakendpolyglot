// 008 · Best Single Trade
Check(BestTrade([8, 3, 6, 1, 7, 4]) == 6);
Check(BestTrade([9, 7, 4, 2]) == 0);
Check(BestTrade([5, 9, 1]) == 4);
Check(BestTrade([3]) == 0);
Check(BestTrade([]) == 0);
Console.WriteLine("008 ok");

static int BestTrade(int[] prices)
{
    var lowest = int.MaxValue;
    var best = 0;
    foreach (var price in prices)
    {
        lowest = Math.Min(lowest, price);
        best = Math.Max(best, price - lowest);
    }
    return best;
}

static void Check(bool ok)
{
    if (!ok) throw new Exception("test failed");
}
