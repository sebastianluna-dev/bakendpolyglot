// 061 · Longest Run of Ones With k Flips
Check(LongestOnes([1, 1, 0, 1, 0, 0, 1, 1, 1, 0, 1], 2) == 6);
Check(LongestOnes([1, 0, 1, 1, 1, 0], 0) == 3);
Check(LongestOnes([], 3) == 0);
Check(LongestOnes([0, 0, 0], 0) == 0);
Check(LongestOnes([0, 0, 0], 5) == 3);
Check(LongestOnes([1, 0, 0, 1], 1) == 2);
Check(LongestOnes([1, 1, 1], 1) == 3);
Console.WriteLine("061 ok");

static int LongestOnes(int[] seats, int k)
{
    int left = 0, zeros = 0, best = 0; // zeros inside seats[left..right]
    for (var right = 0; right < seats.Length; right++)
    {
        if (seats[right] == 0) zeros++;
        while (zeros > k)
        {
            if (seats[left] == 0) zeros--;
            left++;
        }
        best = Math.Max(best, right - left + 1);
    }
    return best;
}

static void Check(bool ok)
{
    if (!ok) throw new Exception("test failed");
}
