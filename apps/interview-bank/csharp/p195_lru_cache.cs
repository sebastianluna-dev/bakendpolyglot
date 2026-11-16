// 195 · LRU Cache
var c = new LruCache(2);
c.Put(1, 10);
c.Put(2, 20);
Check(c.Get(1) == 10);
c.Put(3, 30); // evicts 2
Check(c.Get(2) == -1);
Check(c.Get(3) == 30);
c.Put(1, 11); // an update counts as a use
c.Put(4, 40); // evicts 3
Check(c.Get(3) == -1);
Check(c.Get(1) == 11);
Check(c.Get(4) == 40);

var one = new LruCache(1);
one.Put(5, 50);
Check(one.Get(5) == 50);
one.Put(6, 60);
Check(one.Get(5) == -1);
Check(one.Get(6) == 60);
Check(one.Get(7) == -1);
one.Put(6, 61);
Check(one.Get(6) == 61);
Console.WriteLine("195 ok");

static void Check(bool ok)
{
    if (!ok) throw new Exception("test failed");
}

class Node
{
    public int Key, Value;
    public Node Prev, Next;

    public Node(int key, int value)
    {
        Key = key;
        Value = value;
        Prev = Next = this;
    }
}

class LruCache(int capacity)
{
    private readonly Dictionary<int, Node> nodes = [];
    private readonly Node head = new(0, 0); // sentinel: head.Next is the most recent, head.Prev the least recent

    public int Get(int key)
    {
        if (!nodes.TryGetValue(key, out var node)) return -1;
        Unlink(node);
        PushFront(node);
        return node.Value;
    }

    public void Put(int key, int value)
    {
        if (nodes.TryGetValue(key, out var existing))
        {
            existing.Value = value;
            Unlink(existing);
            PushFront(existing);
            return;
        }
        if (nodes.Count == capacity)
        {
            var oldest = head.Prev;
            Unlink(oldest);
            nodes.Remove(oldest.Key);
        }
        var node = new Node(key, value);
        nodes[key] = node;
        PushFront(node);
    }

    private static void Unlink(Node node)
    {
        node.Prev.Next = node.Next;
        node.Next.Prev = node.Prev;
    }

    private void PushFront(Node node)
    {
        node.Prev = head;
        node.Next = head.Next;
        head.Next.Prev = node;
        head.Next = node;
    }
}
