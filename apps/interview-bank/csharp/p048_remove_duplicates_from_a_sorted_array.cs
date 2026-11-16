// 048 · Remove Duplicates From a Sorted Array
Check(Deduped([3, 3, 5, 8, 8, 8, 9]).SequenceEqual([3, 5, 8, 9]));
Check(Deduped([7, 7, 7]).SequenceEqual([7]));
Check(Deduped([]).Length == 0);
Check(Deduped([1, 2, 3]).SequenceEqual([1, 2, 3]));
Check(Deduped([10, 10, 20, 20, 30]).SequenceEqual([10, 20, 30]));
Console.WriteLine("048 ok");

static int RemoveDuplicates(int[] ids)
{
    if (ids.Length == 0) return 0;
    var write = 1; // ids[0..write) holds the distinct ids kept so far
    for (var read = 1; read < ids.Length; read++)
    {
        if (ids[read] != ids[write - 1]) ids[write++] = ids[read];
    }
    return write;
}

static int[] Deduped(int[] ids)
{
    var k = RemoveDuplicates(ids);
    return ids[..k];
}

static void Check(bool ok)
{
    if (!ok) throw new Exception("test failed");
}
