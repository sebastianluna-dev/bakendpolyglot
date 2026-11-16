// 060 · Two Kinds of Fruit
Check(LongestTwoKinds([3, 3, 7, 3, 9, 9, 7]) == 4);
Check(LongestTwoKinds([5, 5, 5]) == 3);
Check(LongestTwoKinds([]) == 0);
Check(LongestTwoKinds([1, 2, 3, 4]) == 2);
Check(LongestTwoKinds([1, 2, 1, 3, 3, 3, 1]) == 5);
Check(LongestTwoKinds([4]) == 1);
Console.WriteLine("060 ok");

static int LongestTwoKinds(int[] crates)
{
    var inWindow = new Dictionary<int, int>(); // kind -> crates of it in the window
    int left = 0, best = 0;
    for (var right = 0; right < crates.Length; right++)
    {
        inWindow[crates[right]] = inWindow.GetValueOrDefault(crates[right]) + 1;
        while (inWindow.Count > 2)
        {
            var kind = crates[left];
            if (--inWindow[kind] == 0) inWindow.Remove(kind); // a kind with no crates left must not count
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
