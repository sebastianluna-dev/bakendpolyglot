// 088 · Add Two Numbers Stored as Lists
Check(Add([5, 2, 9], [8, 4]).SequenceEqual([3, 7, 9]));
Check(Add([9, 9, 9], [1]).SequenceEqual([0, 0, 0, 1]));
Check(Add([0], [0]).SequenceEqual([0]));
Check(Add([1], [6, 0, 3]).SequenceEqual([7, 0, 3]));
Check(Add([9, 9], [9, 9]).SequenceEqual([8, 9, 1]));
Check(Add([.. Enumerable.Repeat(9, 20)], [1]).SequenceEqual([.. Enumerable.Repeat(0, 20), 1]));
Console.WriteLine("088 ok");

static ListNode? AddLists(ListNode? a, ListNode? b)
{
    var dummy = new ListNode(0);
    var tail = dummy;
    var carry = 0;
    while (a != null || b != null || carry > 0)
    {
        var sum = carry + (a?.Val ?? 0) + (b?.Val ?? 0);
        carry = sum / 10;
        var digit = new ListNode(sum % 10);
        tail.Next = digit;
        tail = digit;
        a = a?.Next;
        b = b?.Next;
    }
    return dummy.Next;
}

static int[] Add(int[] a, int[] b) => ToArray(AddLists(FromArray(a), FromArray(b)));

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
