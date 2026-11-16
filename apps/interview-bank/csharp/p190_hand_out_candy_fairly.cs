// 190 · Hand Out Candy Fairly
Check(MinCandy([1, 3, 2, 2, 1]) == 7);
Check(MinCandy([5, 4, 3, 4]) == 8);
Check(MinCandy([]) == 0);
Check(MinCandy([7]) == 1);
Check(MinCandy([2, 2, 2]) == 3);
Check(MinCandy([1, 2, 3, 4]) == 10);
Check(MinCandy([1, 2, 3, 4, 1]) == 11);
Check(MinCandy([4, 3, 2, 1]) == 10);
Console.WriteLine("190 ok");

static int MinCandy(int[] scores)
{
    var n = scores.Length;
    var candy = new int[n];
    Array.Fill(candy, 1);
    for (var i = 1; i < n; i++)
        if (scores[i] > scores[i - 1]) candy[i] = candy[i - 1] + 1;
    for (var i = n - 2; i >= 0; i--)
        // max keeps what the first pass gave for the left neighbour
        if (scores[i] > scores[i + 1]) candy[i] = Math.Max(candy[i], candy[i + 1] + 1);
    return candy.Sum();
}

static void Check(bool ok)
{
    if (!ok) throw new Exception("test failed");
}
