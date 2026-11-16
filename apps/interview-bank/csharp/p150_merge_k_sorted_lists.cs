// 150 · Merge k Sorted Lists
Check(Merge([[1, 4, 7], [2, 5], [0, 6, 9]]).SequenceEqual([0, 1, 2, 4, 5, 6, 7, 9]));
Check(Merge([[], [3], []]).SequenceEqual([3]));
Check(Merge([]).Length == 0);
Check(Merge([[]]).Length == 0);
Check(Merge([[2, 2], [2], [1, 2]]).SequenceEqual([1, 2, 2, 2, 2]));
Check(Merge([[-5, 0, 5]]).SequenceEqual([-5, 0, 5]));
Check(Merge([[10], [-10], [0], [-20, 20]]).SequenceEqual([-20, -10, 0, 10, 20]));
Console.WriteLine("150 ok");

static ListNode? MergeLists(ListNode?[] lists)
{
    var heap = new PriorityQueue<ListNode, int>(); // the front node of each list, by value
    foreach (var head in lists)
    {
        if (head != null) heap.Enqueue(head, head.Val);
    }
    var dummy = new ListNode(0);
    var tail = dummy;
    while (heap.TryDequeue(out var node, out _))
    {
        tail.Next = node;
        tail = node;
        if (node.Next != null) heap.Enqueue(node.Next, node.Next.Val);
    }
    return dummy.Next;
}

static int[] Merge(int[][] lists) => ToArray(MergeLists([.. lists.Select(FromArray)]));

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
