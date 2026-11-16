// 082 · Merge Two Sorted Lists
Check(Merge([1, 4, 6], [2, 3, 6, 9]).SequenceEqual([1, 2, 3, 4, 6, 6, 9]));
Check(Merge([], [5, 8]).SequenceEqual([5, 8]));
Check(Merge([], []).Length == 0);
Check(Merge([7, 8], [1, 2]).SequenceEqual([1, 2, 7, 8]));
Check(Merge([-3, 0], [-3]).SequenceEqual([-3, -3, 0]));
var first = FromArray([2]);
var second = FromArray([2]);
var merged = MergeSorted(first, second);
Check(merged == first && merged?.Next == second);
Console.WriteLine("082 ok");

static ListNode? MergeSorted(ListNode? a, ListNode? b)
{
    var dummy = new ListNode(0);
    var tail = dummy;
    while (a != null && b != null)
    {
        if (a.Val <= b.Val)
        {
            tail.Next = a;
            tail = a;
            a = a.Next;
        }
        else
        {
            tail.Next = b;
            tail = b;
            b = b.Next;
        }
    }
    tail.Next = a ?? b; // whatever is left is already sorted
    return dummy.Next;
}

static int[] Merge(int[] a, int[] b) => ToArray(MergeSorted(FromArray(a), FromArray(b)));

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
