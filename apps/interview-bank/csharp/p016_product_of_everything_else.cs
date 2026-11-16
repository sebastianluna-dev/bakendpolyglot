// 016 · Product of Everything Else
Check(ProductOfOthers([2, 3, 4, 5]).SequenceEqual([60, 40, 30, 24]));
Check(ProductOfOthers([4, 0, 5]).SequenceEqual([0, 20, 0]));
Check(ProductOfOthers([3, 0, 2, 0]).SequenceEqual([0, 0, 0, 0]));
Check(ProductOfOthers([-1, 2, -3]).SequenceEqual([-6, 3, -2]));
Check(ProductOfOthers([7]).SequenceEqual([1]));
Check(ProductOfOthers([]).Length == 0);
Console.WriteLine("016 ok");

static int[] ProductOfOthers(int[] nums)
{
    var output = new int[nums.Length];
    var left = 1;
    for (var i = 0; i < nums.Length; i++)
    {
        output[i] = left; // product of everything before i
        left *= nums[i];
    }
    var right = 1;
    for (var i = nums.Length - 1; i >= 0; i--)
    {
        output[i] *= right;
        right *= nums[i];
    }
    return output;
}

static void Check(bool ok)
{
    if (!ok) throw new Exception("test failed");
}
