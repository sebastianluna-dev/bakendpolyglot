// 050 · Squares of a Sorted Array
Check(SortedSquares([-5, -2, 0, 3, 4]).SequenceEqual([0, 4, 9, 16, 25]));
Check(SortedSquares([-3, -3, -1]).SequenceEqual([1, 9, 9]));
Check(SortedSquares([]).Length == 0);
Check(SortedSquares([2, 5]).SequenceEqual([4, 25]));
Check(SortedSquares([-7]).SequenceEqual([49]));
Check(SortedSquares([-4, -1, 1, 4]).SequenceEqual([1, 1, 16, 16]));
Console.WriteLine("050 ok");

static int[] SortedSquares(int[] nums)
{
    var output = new int[nums.Length];
    int lo = 0, hi = nums.Length - 1;
    for (var k = nums.Length - 1; k >= 0; k--)
    {
        // the largest square left is at one of the two ends
        if (Math.Abs(nums[lo]) > Math.Abs(nums[hi]))
        {
            output[k] = nums[lo] * nums[lo];
            lo++;
        }
        else
        {
            output[k] = nums[hi] * nums[hi];
            hi--;
        }
    }
    return output;
}

static void Check(bool ok)
{
    if (!ok) throw new Exception("test failed");
}
