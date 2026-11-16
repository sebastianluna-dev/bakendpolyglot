// 012 · Shared Members of Two Lists
Check(SharedMembers([104, 211, 104, 350, 87], [87, 104, 999, 104]).SequenceEqual([104, 87]));
Check(SharedMembers([1, 2], [3, 4]).Length == 0);
Check(SharedMembers([], [1]).Length == 0);
Check(SharedMembers([5, 5, 5], [5]).SequenceEqual([5]));
Check(SharedMembers([3, 1, 2], [2, 3]).SequenceEqual([3, 2]));
Console.WriteLine("012 ok");

static int[] SharedMembers(int[] first, int[] second)
{
    var pending = new HashSet<int>(second);
    var shared = new List<int>();
    foreach (var id in first)
    {
        if (pending.Remove(id)) shared.Add(id); // removed, so a repeat in first is skipped
    }
    return shared.ToArray();
}

static void Check(bool ok)
{
    if (!ok) throw new Exception("test failed");
}
