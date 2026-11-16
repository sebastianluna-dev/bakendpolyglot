// 090 · Reverse in Groups of k
Check(Groups([1, 2, 3, 4, 5, 6, 7, 8], 3).SequenceEqual([3, 2, 1, 6, 5, 4, 7, 8]));
Check(Groups([1, 2, 3, 4, 5, 6], 2).SequenceEqual([2, 1, 4, 3, 6, 5]));
Check(Groups([4, 5, 6], 1).SequenceEqual([4, 5, 6]));
Check(Groups([4, 5, 6], 3).SequenceEqual([6, 5, 4]));
Check(Groups([4, 5, 6], 4).SequenceEqual([4, 5, 6]));
Check(Groups([], 2).Length == 0);
var head = FromArray([1, 2, 3]);
Check(ReverseInGroups(head, 3)?.Next?.Next == head);
Console.WriteLine("090 ok");

static ListNode? ReverseInGroups(ListNode? head, int k)
{
    var dummy = new ListNode(0, head);
    var before = dummy; // the node just before the next group
    while (true)
    {
        ListNode? last = before;
        for (var i = 0; i < k && last != null; i++) last = last.Next;
        if (last == null) return dummy.Next; // fewer than k nodes left: keep them as they are
        var after = last.Next;
        var first = before.Next!;
        var prev = after;
        ListNode? node = first;
        while (node != after)
        {
            var next = node!.Next;
            node.Next = prev;
            prev = node;
            node = next;
        }
        before.Next = last;
        before = first;
    }
}

static int[] Groups(int[] values, int k) => ToArray(ReverseInGroups(FromArray(values), k));

static ListNode? FromArray(int[] values)
{
    ListNode? head = null;
    for (var i = values.Length - 1; i >= 0; i--) head = new ListNode(values[i], head);
    return head;
}

static int[] ToArray(ListNode? head)
{
    var values = new List<int>();
    for (var node = head; node != null; node = node.Next) values.Add(node.Val);
    return [.. values];
}

static void Check(bool ok)
{
    if (!ok) throw new Exception("test failed");
}

class ListNode(int val, ListNode? next = null)
{
    public int Val = val;
    public ListNode? Next = next;
}
