// 013 · Collapse Consecutive IDs Into Ranges
Check(CollapseRanges([1, 2, 3, 7, 9, 10]).SequenceEqual(["1-3", "7", "9-10"]));
Check(CollapseRanges([0, 2, 4]).SequenceEqual(["0", "2", "4"]));
Check(CollapseRanges([]).Count == 0);
Check(CollapseRanges([5]).SequenceEqual(["5"]));
Check(CollapseRanges([40, 41, 42, 43]).SequenceEqual(["40-43"]));
Console.WriteLine("013 ok");

static List<string> CollapseRanges(int[] ids)
{
    var ranges = new List<string>();
    var i = 0;
    while (i < ids.Length)
    {
        var start = ids[i];
        while (i + 1 < ids.Length && ids[i + 1] == ids[i] + 1) i++;
        ranges.Add(start == ids[i] ? $"{start}" : $"{start}-{ids[i]}");
        i++;
    }
    return ranges;
}

static void Check(bool ok)
{
    if (!ok) throw new Exception("test failed");
}
