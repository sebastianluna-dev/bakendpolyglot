// 087 · Interleave a List With Its Reverse
Check(Interleaved([1, 2, 3, 4, 5, 6]).SequenceEqual([1, 6, 2, 5, 3, 4]));
Check(Interleaved([3, 8, 5, 2, 9]).SequenceEqual([3, 9, 8, 2, 5]));
Check(Interleaved([]).Length == 0);
Check(Interleaved([7]).SequenceEqual([7]));
Check(Interleaved([1, 2]).SequenceEqual([1, 2]));
Check(Interleaved([10, 20, 30]).SequenceEqual([10, 30, 20]));
Check(Interleaved([1, 2, 3, 4]).SequenceEqual([1, 4, 2, 3]));
Console.WriteLine("087 ok");

static void InterleaveWithReverse(ListNode? head)
{
    if (head == null) return;
    var slow = head;
    var fast = head;
    while (fast.Next != null && fast.Next.Next != null)
    {
        slow = slow.Next!; // slow is behind fast, so it is not null here
        fast = fast.Next.Next;
    }
    var second = Reverse(slow.Next);
    slow.Next = null; // cut the first half off
    ListNode? first = head;
    while (first != null && second != null)
    {
        var nextFirst = first.Next;
        var nextSecond = second.Next;
        first.Next = second;
        second.Next = nextFirst;
        first = nextFirst;
        second = nextSecond;
    }
}

static ListNode? Reverse(ListNode? head)
{
    ListNode? prev = null;
    var node = head;
    while (node != null)
    {
        var next = node.Next;
        node.Next = prev;
        prev = node;
        node = next;
    }
    return prev;
}

static int[] Interleaved(int[] values)
{
    var head = FromArray(values);
    InterleaveWithReverse(head);
    return ToArray(head);
}

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
