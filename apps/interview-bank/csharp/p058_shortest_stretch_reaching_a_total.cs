// 058 · Shortest Stretch Reaching a Total
Check(ShortestReaching([3, 1, 4, 1, 5, 9, 2], 15) == 3);
Check(ShortestReaching([2, 2, 2], 7) == 0);
Check(ShortestReaching([], 5) == 0);
Check(ShortestReaching([8], 8) == 1);
Check(ShortestReaching([1, 1, 1, 1], 4) == 4);
Check(ShortestReaching([1, 6, 1, 1, 7], 7) == 1);
Console.WriteLine("058 ok");

static int ShortestReaching(int[] weights, int target)
{
    var n = weights.Length;
    var best = n + 1; // longer than any real stretch: means "none found"
    int left = 0, sum = 0;
    for (var right = 0; right < n; right++)
    {
        sum += weights[right];
        while (sum >= target)
        {
            best = Math.Min(best, right - left + 1);
            sum -= weights[left];
            left++;
        }
    }
    return best == n + 1 ? 0 : best;
}

static void Check(bool ok)
{
    if (!ok) throw new Exception("test failed");
}
