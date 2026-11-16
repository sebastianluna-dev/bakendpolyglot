// 023 · A Set With Random Pick in O(1)
var calls = 0;
var set = new RandomSet(n => calls++ % n);
Check(set.Add(12));
Check(set.Add(7));
Check(!set.Add(12));
Check(set.Add(30));
Check(set.Remove(7));
Check(!set.Remove(7));
Check(!set.Remove(99));
Check(Picks(set, 2).SequenceEqual([12, 30]));
Check(set.Add(5));
Check(Picks(set, 3).SequenceEqual([5, 12, 30]));
Check(set.Remove(30));
Check(set.Remove(5));
Check(set.Add(5));
Check(set.Remove(12));
Check(set.Pick() == 5);
Console.WriteLine("023 ok");

static int[] Picks(RandomSet set, int count)
{
    var result = new int[count];
    for (var k = 0; k < count; k++) result[k] = set.Pick();
    Array.Sort(result);
    return result;
}

static void Check(bool ok)
{
    if (!ok) throw new Exception("test failed");
}

class RandomSet(Func<int, int> randomIndex)
{
    private readonly List<int> items = [];
    private readonly Dictionary<int, int> position = []; // value -> its index in items

    public bool Add(int value)
    {
        if (!position.TryAdd(value, items.Count)) return false;
        items.Add(value);
        return true;
    }

    public bool Remove(int value)
    {
        if (!position.TryGetValue(value, out var i)) return false;
        var last = items[^1];
        items.RemoveAt(items.Count - 1);
        if (last != value)
        {
            items[i] = last;
            position[last] = i;
        }
        position.Remove(value);
        return true;
    }

    public int Pick() => items[randomIndex(items.Count)];
}
