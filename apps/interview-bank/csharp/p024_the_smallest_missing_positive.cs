// 024 · The Smallest Missing Positive
Check(SmallestMissingPositive([3, 1, -2, 2, 7]) == 4);
Check(SmallestMissingPositive([5, 2, 2, 9]) == 1);
Check(SmallestMissingPositive([]) == 1);
Check(SmallestMissingPositive([1, 2, 3]) == 4);
Check(SmallestMissingPositive([0, -1, -5]) == 1);
Check(SmallestMissingPositive([1, 1]) == 2);
Check(SmallestMissingPositive([2, 3, 4, 1]) == 5);
Check(SmallestMissingPositive([2147483647, 1]) == 2);
Console.WriteLine("024 ok");

static int SmallestMissingPositive(int[] nums)
{
    var n = nums.Length;
    for (var i = 0; i < n; i++)
    {
        // keep swapping until slot i holds a value that has no slot or is already in its slot
        while (nums[i] >= 1 && nums[i] <= n && nums[nums[i] - 1] != nums[i])
        {
            var j = nums[i] - 1;
            (nums[i], nums[j]) = (nums[j], nums[i]);
        }
    }
    for (var i = 0; i < n; i++)
    {
        if (nums[i] != i + 1) return i + 1;
    }
    return n + 1;
}

static void Check(bool ok)
{
    if (!ok) throw new Exception("test failed");
}
