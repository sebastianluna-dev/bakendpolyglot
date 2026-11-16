// 059 · Sort Three Colours in One Pass
Check(Sorted([2, 0, 1, 2, 1, 0]).SequenceEqual([0, 0, 1, 1, 2, 2]));
Check(Sorted([1, 1, 1]).SequenceEqual([1, 1, 1]));
Check(Sorted([]).Length == 0);
Check(Sorted([2, 0]).SequenceEqual([0, 2]));
Check(Sorted([2, 2, 0, 0]).SequenceEqual([0, 0, 2, 2]));
Check(Sorted([0, 2, 1]).SequenceEqual([0, 1, 2]));
Check(Sorted([1, 2, 0]).SequenceEqual([0, 1, 2]));
Check(Sorted([1, 2, 0, 2, 0, 1, 0]).SequenceEqual([0, 0, 0, 1, 1, 2, 2]));
Console.WriteLine("059 ok");

static void SortColours(int[] stickers)
{
    int low = 0, mid = 0, high = stickers.Length - 1; // [0, low) is 0, [low, mid) is 1, (high, end] is 2
    while (mid <= high)
    {
        if (stickers[mid] == 0)
        {
            (stickers[low], stickers[mid]) = (stickers[mid], stickers[low]);
            low++;
            mid++;
        }
        else if (stickers[mid] == 1)
        {
            mid++;
        }
        else
        {
            (stickers[mid], stickers[high]) = (stickers[high], stickers[mid]);
            high--; // mid stays: the value swapped in has not been looked at yet
        }
    }
}

static int[] Sorted(int[] stickers)
{
    SortColours(stickers);
    return stickers;
}

static void Check(bool ok)
{
    if (!ok) throw new Exception("test failed");
}
