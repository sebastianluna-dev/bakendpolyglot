// 200 · Consistent Hash Ring
using System.Text;

Check(HashRing.Fnv1a("") == 0x811c9dc5);
Check(HashRing.Fnv1a("a") == 0xe40c292c);
Check(HashRing.Fnv1a("foobar") == 0xbf9cf968);
Check(HashRing.Mix32(0) == 0);
Check(HashRing.RingHash("cache-a#0") == 0x6901e554);
Check(HashRing.RingHash("book:37") == 0xfcfa1e45);

var ring = new HashRing(3);
Check(ring.NodeFor("book:1") == "");
foreach (var name in new[] { "cache-a", "cache-b", "cache-c" }) ring.AddNode(name);
Check(Owners(ring) == "babccccc");
ring.AddNode("cache-a");
ring.RemoveNode("cache-z");
Check(Owners(ring) == "babccccc");
ring.RemoveNode("cache-b");
Check(Owners(ring) == "aacccccc"); // only cache-b's keys moved
ring.AddNode("cache-b");
Check(Owners(ring) == "babccccc");
ring.AddNode("cache-d");
Check(Owners(ring) == "babdccdc"); // keys move only to the new node
foreach (var name in new[] { "cache-a", "cache-b", "cache-c", "cache-d" }) ring.RemoveNode(name);
Check(ring.NodeFor("book:1") == "");
Console.WriteLine("200 ok");

static string Owners(HashRing ring)
{
    string[] keys = ["book:1", "book:2", "book:3", "book:4", "book:5", "book:6", "book:7", "book:37"];
    return string.Concat(keys.Select(k => ring.NodeFor(k)[^1..])); // the last letter of each key's node
}

static void Check(bool ok)
{
    if (!ok) throw new Exception("test failed");
}

class HashRing(int replicas)
{
    private readonly HashSet<string> nodes = [];
    private readonly List<(uint Hash, string Node)> points = []; // sorted by hash, then node

    public static uint Fnv1a(string s)
    {
        var h = 2166136261u;
        foreach (var b in Encoding.UTF8.GetBytes(s))
        {
            h ^= b;
            h *= 16777619; // uint arithmetic wraps at 2^32
        }
        return h;
    }

    public static uint Mix32(uint h)
    {
        h ^= h >> 16;
        h *= 0x85ebca6b;
        h ^= h >> 13;
        h *= 0xc2b2ae35;
        h ^= h >> 16;
        return h;
    }

    public static uint RingHash(string s) => Mix32(Fnv1a(s));

    public void AddNode(string node)
    {
        if (!nodes.Add(node)) return;
        for (var i = 0; i < replicas; i++) points.Add((RingHash($"{node}#{i}"), node));
        points.Sort((x, y) => x.Hash != y.Hash ? x.Hash.CompareTo(y.Hash) : string.CompareOrdinal(x.Node, y.Node));
    }

    public void RemoveNode(string node)
    {
        if (!nodes.Remove(node)) return;
        points.RemoveAll(p => p.Node == node);
    }

    public string NodeFor(string key)
    {
        if (points.Count == 0) return "";
        var h = RingHash(key);
        int lo = 0, hi = points.Count; // looking for the first point with hash >= h
        while (lo < hi)
        {
            var mid = lo + (hi - lo) / 2;
            if (points[mid].Hash >= h) hi = mid;
            else lo = mid + 1;
        }
        return points[lo % points.Count].Node; // past the last point: wrap to the first
    }
}
