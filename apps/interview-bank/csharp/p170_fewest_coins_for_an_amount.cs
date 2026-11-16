// 170 · Fewest Coins for an Amount
Check(FewestCoins([1, 4, 6], 8) == 2);
Check(FewestCoins([5, 10], 3) == -1);
Check(FewestCoins([7], 0) == 0);
Check(FewestCoins([2], 3) == -1);
Check(FewestCoins([3, 7], 20) == 4);
Check(FewestCoins([2, 5, 10, 25], 99) == 7);
Check(FewestCoins([4, 7], 5000) == 716);
Console.WriteLine("170 ok");

static int FewestCoins(int[] coins, int amount)
{
    var unreachable = amount + 1; // more coins than any real answer needs
    var fewest = new int[amount + 1];
    Array.Fill(fewest, unreachable);
    fewest[0] = 0;
    for (var a = 1; a <= amount; a++)
    {
        foreach (var coin in coins)
        {
            if (coin <= a) fewest[a] = Math.Min(fewest[a], fewest[a - coin] + 1);
        }
    }
    return fewest[amount] == unreachable ? -1 : fewest[amount];
}

static void Check(bool ok)
{
    if (!ok) throw new Exception("test failed");
}
