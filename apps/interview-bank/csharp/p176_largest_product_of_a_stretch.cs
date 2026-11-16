// 176 · Largest Product of a Stretch
Check(LargestProduct([2, -5, -2, 4, 0, 3]) == 80);
Check(LargestProduct([-3, 0, -2]) == 0);
Check(LargestProduct([-2]) == -2);
Check(LargestProduct([-2, 3, -4]) == 24);
Check(LargestProduct([0, 0]) == 0);
Check(LargestProduct([-1, -1, -1]) == 1);
Check(LargestProduct([3, -1, 4]) == 4);
Check(LargestProduct([-4, 0, -5, 0, -6, 2]) == 2);
Check(LargestProduct(Enumerable.Repeat(10, 9).ToArray()) == 1000000000);
Console.WriteLine("176 ok");

static int LargestProduct(int[] nums)
{
    var best = nums[0];
    var hi = nums[0]; // largest product of a stretch that ends at the current element
    var lo = nums[0]; // smallest one: a negative number turns it into the largest
    for (var i = 1; i < nums.Length; i++)
    {
        var x = nums[i];
        int a = hi * x, b = lo * x;
        hi = Math.Max(x, Math.Max(a, b));
        lo = Math.Min(x, Math.Min(a, b));
        best = Math.Max(best, hi);
    }
    return best;
}

static void Check(bool ok)
{
    if (!ok) throw new Exception("test failed");
}
