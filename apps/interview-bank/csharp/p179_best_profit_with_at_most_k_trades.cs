// 179 · Best Profit With at Most k Trades
int[] prices = [3, 8, 5, 1, 7, 8];
int[] zigzag = [2, 5, 1, 4, 3, 6];
Check(BestProfit(prices, 2) == 12);
Check(BestProfit(prices, 1) == 7);
Check(BestProfit([9, 7, 4, 1], 2) == 0);
Check(BestProfit(prices, 0) == 0);
Check(BestProfit([], 3) == 0);
Check(BestProfit([7], 1) == 0);
Check(BestProfit([1, 2, 3, 4, 5], 2) == 4);
Check(BestProfit(zigzag, 1) == 5);
Check(BestProfit(zigzag, 2) == 8);
Check(BestProfit(zigzag, 3) == 9);
Check(BestProfit(zigzag, 10) == 9);
Check(BestProfit([1, 3, 2, 8], 1) == 7);
Console.WriteLine("179 ok");

static int BestProfit(int[] prices, int k)
{
    if (prices.Length == 0 || k == 0) return 0;
    var bought = new int[k + 1]; // bought[t]: most cash holding the t-th share
    Array.Fill(bought, -prices[0]);
    var sold = new int[k + 1]; // sold[t]: most cash after t complete trades
    foreach (var p in prices)
    {
        for (var t = 1; t <= k; t++)
        {
            bought[t] = Math.Max(bought[t], sold[t - 1] - p);
            sold[t] = Math.Max(sold[t], bought[t] + p);
        }
    }
    return sold[k];
}

static void Check(bool ok)
{
    if (!ok) throw new Exception("test failed");
}
