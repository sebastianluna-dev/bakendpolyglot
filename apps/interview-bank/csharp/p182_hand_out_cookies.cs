// 182 · Hand Out Cookies
Check(ContentChildren([5, 2, 8, 2], [3, 1, 6, 9]) == 3);
Check(ContentChildren([1, 3], [3, 1]) == 2);
Check(ContentChildren([5, 2, 8, 2], [3, 1, 6]) == 2);
Check(ContentChildren([4, 4], []) == 0);
Check(ContentChildren([], [1, 2]) == 0);
Check(ContentChildren([10, 20], [1, 2, 3]) == 0);
Check(ContentChildren([2, 2, 2], [2, 2]) == 2);
Check(ContentChildren([1, 2, 3], [3]) == 1);
Console.WriteLine("182 ok");

static int ContentChildren(int[] greed, int[] sizes)
{
    Array.Sort(greed);
    Array.Sort(sizes);
    var child = 0; // the least greedy child still without a cookie
    foreach (var size in sizes)
    {
        if (child < greed.Length && size >= greed[child]) child++;
    }
    return child;
}

static void Check(bool ok)
{
    if (!ok) throw new Exception("test failed");
}
