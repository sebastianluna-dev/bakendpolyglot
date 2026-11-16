// 089 · Deep-Copy a List With Random Links
CheckCopy([(4, 2), (9, 0), (6, -1)]);
CheckCopy([(1, 1), (2, 1)]);
CheckCopy([]);
CheckCopy([(5, 0)]);
CheckCopy([(7, 1), (7, 0)]);
CheckCopy([(3, -1), (8, -1), (2, -1)]);
Console.WriteLine("089 ok");

static ListNode? DeepCopy(ListNode? head)
{
    var copyOf = new Dictionary<ListNode, ListNode>(); // original node -> its copy
    for (var node = head; node != null; node = node.Next) copyOf[node] = new ListNode(node.Val);
    for (var node = head; node != null; node = node.Next)
    {
        var copy = copyOf[node];
        copy.Next = node.Next == null ? null : copyOf[node.Next];
        copy.Random = node.Random == null ? null : copyOf[node.Random];
    }
    return head == null ? null : copyOf[head];
}

// Each pair is (value, index of the random target), with -1 for no target.
static ListNode? FromPairs((int Val, int Random)[] pairs)
{
    var nodes = pairs.Select(p => new ListNode(p.Val)).ToArray();
    for (var i = 0; i < nodes.Length; i++)
    {
        nodes[i].Next = i + 1 < nodes.Length ? nodes[i + 1] : null;
        if (pairs[i].Random >= 0) nodes[i].Random = nodes[pairs[i].Random];
    }
    return nodes.Length > 0 ? nodes[0] : null;
}

static (int Val, int Random)[] ToPairs(ListNode? head)
{
    var index = new Dictionary<ListNode, int>();
    var i = 0;
    for (var node = head; node != null; node = node.Next) index[node] = i++;
    var pairs = new List<(int, int)>();
    for (var node = head; node != null; node = node.Next)
    {
        pairs.Add((node.Val, node.Random == null ? -1 : index[node.Random]));
    }
    return [.. pairs];
}

static bool SharesNode(ListNode? a, ListNode? b)
{
    var seen = new HashSet<ListNode>();
    for (var node = a; node != null; node = node.Next) seen.Add(node);
    for (var node = b; node != null; node = node.Next)
    {
        if (seen.Contains(node) || (node.Random != null && seen.Contains(node.Random))) return true;
    }
    return false;
}

static void CheckCopy((int Val, int Random)[] pairs)
{
    var original = FromPairs(pairs);
    var copy = DeepCopy(original);
    Check(ToPairs(copy).SequenceEqual(pairs));
    Check(ToPairs(original).SequenceEqual(pairs));
    Check(!SharesNode(original, copy));
}

static void Check(bool ok)
{
    if (!ok) throw new Exception("test failed");
}

class ListNode(int val, ListNode? next = null)
{
    public int Val = val;
    public ListNode? Next = next;
    public ListNode? Random;
}
