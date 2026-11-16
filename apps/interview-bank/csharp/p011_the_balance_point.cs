// 011 · The Balance Point
Check(BalancePoint([3, 1, 5, 2, 2]) == 2);
Check(BalancePoint([1, 2, 3]) == -1);
Check(BalancePoint([]) == -1);
Check(BalancePoint([7]) == 0);
Check(BalancePoint([5, 2, -2]) == 0);
Check(BalancePoint([0, 0, 0]) == 0);
Console.WriteLine("011 ok");

static int BalancePoint(int[] nums)
{
    var total = nums.Sum();
    var left = 0;
    for (var i = 0; i < nums.Length; i++)
    {
        if (left == total - left - nums[i]) return i;
        left += nums[i];
    }
    return -1;
}

static void Check(bool ok)
{
    if (!ok) throw new Exception("test failed");
}
