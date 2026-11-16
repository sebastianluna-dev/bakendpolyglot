// 051 · Best Average Over k Days
Check(Close(BestAverage([4, -2, 7, 5, -1, 6], 4), 4.25));
Check(Close(BestAverage([-3, -1, -4], 2), -2.0));
Check(Close(BestAverage([5], 1), 5.0));
Check(Close(BestAverage([1, 2, 3, 4], 1), 4.0));
Check(Close(BestAverage([-7, -7, -7], 3), -7.0));
Check(Close(BestAverage([1, 1, 2], 3), 4.0 / 3));
Console.WriteLine("051 ok");

static double BestAverage(int[] balances, int k)
{
    var sum = 0;
    for (var i = 0; i < k; i++) sum += balances[i];
    var best = sum;
    for (var i = k; i < balances.Length; i++)
    {
        sum += balances[i] - balances[i - k]; // the day entering minus the day leaving
        best = Math.Max(best, sum);
    }
    return (double)best / k;
}

static bool Close(double got, double want) => Math.Abs(got - want) < 1e-9;

static void Check(bool ok)
{
    if (!ok) throw new Exception("test failed");
}
