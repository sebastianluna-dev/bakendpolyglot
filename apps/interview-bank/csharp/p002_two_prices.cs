// 002 · Two Prices for a Budget
Check(TwoPrices([12, 7, 3, 9, 5], 12).SequenceEqual([2, 3]));
Check(TwoPrices([4, 4, 1], 8).SequenceEqual([0, 1]));
Check(TwoPrices([1, 2], 10).Length == 0);
Console.WriteLine("002 ok");

static int[] TwoPrices(int[] prices, int budget)
{
    var firstIndex = new Dictionary<int, int>(); // price -> earliest index
    for (var j = 0; j < prices.Length; j++)
    {
        if (firstIndex.TryGetValue(budget - prices[j], out var i)) return [i, j];
        firstIndex.TryAdd(prices[j], j);
    }
    return [];
}

static void Check(bool ok)
{
    if (!ok) throw new Exception("test failed");
}
