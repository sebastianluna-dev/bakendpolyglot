// 086 · Remove the k-th Node From the End
Check(Remove([10, 20, 30, 40, 50], 2).SequenceEqual([10, 20, 30, 50]));
Check(Remove([8], 1).Length == 0);
Check(Remove([1, 2], 2).SequenceEqual([2]));
Check(Remove([1, 2], 1).SequenceEqual([1]));
Check(Remove([5, 6, 7], 3).SequenceEqual([6, 7]));
Check(Remove([5, 6, 7], 1).SequenceEqual([5, 6]));
Console.WriteLine("086 ok");

static ListNode? RemoveKthFromEnd(ListNode? head, int k)
{
    var dummy = new ListNode(0, head); // the node before the head, in case the head goes
    var lead = head;
    for (var i = 0; i < k; i++) lead = lead!.Next;
    var trail = dummy;
    while (lead != null)
    {
        lead = lead.Next;
        trail = trail.Next!;
    }
    trail.Next = trail.Next!.Next;
    return dummy.Next;
}

static int[] Remove(int[] values, int k) => ToArray(RemoveKthFromEnd(FromArray(values), k));

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
