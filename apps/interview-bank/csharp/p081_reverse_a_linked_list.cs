// 081 · Reverse a Linked List
Check(ToArray(ReverseList(FromArray([4, 8, 15, 16, 23]))).SequenceEqual([23, 16, 15, 8, 4]));
Check(ToArray(ReverseList(FromArray([2, 9]))).SequenceEqual([9, 2]));
Check(ToArray(ReverseList(FromArray([7]))).SequenceEqual([7]));
Check(ReverseList(FromArray([])) == null);
Check(ToArray(ReverseList(FromArray([5, 5, 1]))).SequenceEqual([1, 5, 5]));
Console.WriteLine("081 ok");

static ListNode? ReverseList(ListNode? head)
{
    ListNode? prev = null;
    var node = head;
    while (node != null)
    {
        var next = node.Next; // save it before the link is overwritten
        node.Next = prev;
        prev = node;
        node = next;
    }
    return prev;
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
