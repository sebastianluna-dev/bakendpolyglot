// 084 · The Middle Node
Check(FromMiddle([3, 1, 4, 1, 5]).SequenceEqual([4, 1, 5]));
Check(FromMiddle([2, 7, 1, 8]).SequenceEqual([1, 8]));
Check(FromMiddle([6]).SequenceEqual([6]));
Check(MiddleNode(null) == null);
Check(FromMiddle([9, 4]).SequenceEqual([4]));
var head = FromArray([10, 20, 30]);
Check(MiddleNode(head) == head?.Next);
Console.WriteLine("084 ok");

static ListNode? MiddleNode(ListNode? head)
{
    var slow = head;
    var fast = head;
    while (fast != null && fast.Next != null)
    {
        slow = slow!.Next; // slow is behind fast, so it is not null here
        fast = fast.Next.Next;
    }
    return slow;
}

static int[] FromMiddle(int[] values) => ToArray(MiddleNode(FromArray(values)));

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
