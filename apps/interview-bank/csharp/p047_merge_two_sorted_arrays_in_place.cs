// 047 · Merge Two Sorted Arrays in Place
Check(Merged([1, 4, 7, 0, 0], 3, [2, 8]).SequenceEqual([1, 2, 4, 7, 8]));
Check(Merged([0, 0], 0, [3, 5]).SequenceEqual([3, 5]));
Check(Merged([2, 3], 2, []).SequenceEqual([2, 3]));
Check(Merged([1, 2, 2, 0, 0, 0], 3, [2, 2, 3]).SequenceEqual([1, 2, 2, 2, 2, 3]));
Check(Merged([-3, 5, 0], 2, [-4]).SequenceEqual([-4, -3, 5]));
Check(Merged([4, 5, 0], 2, [1]).SequenceEqual([1, 4, 5]));
Console.WriteLine("047 ok");

static void MergeInPlace(int[] a, int m, int[] b)
{
    int i = m - 1, j = b.Length - 1;
    var k = m + b.Length - 1; // next slot to fill, from the back
    while (j >= 0)
    {
        if (i >= 0 && a[i] > b[j]) a[k--] = a[i--];
        else a[k--] = b[j--];
    }
}

static int[] Merged(int[] a, int m, int[] b)
{
    MergeInPlace(a, m, b);
    return a;
}

static void Check(bool ok)
{
    if (!ok) throw new Exception("test failed");
}
