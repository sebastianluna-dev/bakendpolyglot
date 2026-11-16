// 018 · Count Stretches With a Target Sum
Check(CountStretches([1, 2, 3, -2, 2], 3) == 4);
Check(CountStretches([0, 0, 0], 0) == 6);
Check(CountStretches([], 0) == 0);
Check(CountStretches([5], 3) == 0);
Check(CountStretches([3], 3) == 1);
Check(CountStretches([1, -1, 1, -1], 0) == 4);
Console.WriteLine("018 ok");

static int CountStretches(int[] changes, int target)
{
    var seen = new Dictionary<int, int> { [0] = 1 }; // prefix sum -> how many times; 0 is the empty prefix
    var sum = 0;
    var count = 0;
    foreach (var change in changes)
    {
        sum += change;
        count += seen.GetValueOrDefault(sum - target);
        seen[sum] = seen.GetValueOrDefault(sum) + 1;
    }
    return count;
}

static void Check(bool ok)
{
    if (!ok) throw new Exception("test failed");
}
