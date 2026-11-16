// 085 · Drop Repeated Values From a Sorted List
Check(Drop([1, 1, 2, 3, 3, 3]).SequenceEqual([1, 2, 3]));
Check(Drop([4, 4, 4]).SequenceEqual([4]));
Check(Drop([]).Length == 0);
Check(Drop([1, 2, 3]).SequenceEqual([1, 2, 3]));
Check(Drop([-2, -2, 0, 5, 5]).SequenceEqual([-2, 0, 5]));
Check(Drop([8]).SequenceEqual([8]));
Console.WriteLine("085 ok");

static ListNode? DropRepeats(ListNode? head)
{
    var node = head;
    while (node != null && node.Next != null)
    {
        if (node.Next.Val == node.Val) node.Next = node.Next.Next; // stay: the new next may repeat too
        else node = node.Next;
    }
    return head;
}

static int[] Drop(int[] values) => ToArray(DropRepeats(FromArray(values)));

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
