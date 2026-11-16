// 083 · Does the List Loop?
Check(HasLoop(WithLoop([3, 8, 1, 6], 1)));
Check(!HasLoop(WithLoop([4, 2], -1)));
Check(HasLoop(WithLoop([5], 0)));
Check(!HasLoop(WithLoop([], -1)));
Check(!HasLoop(WithLoop([2, 5, 2], -1)));
Check(HasLoop(WithLoop([1, 2, 3, 4, 5, 6], 0)));
Check(HasLoop(WithLoop([7, 1], 1)));
Console.WriteLine("083 ok");

static bool HasLoop(ListNode? head)
{
    var slow = head;
    var fast = head;
    while (fast != null && fast.Next != null)
    {
        slow = slow!.Next; // slow is behind fast, so it is not null here
        fast = fast.Next.Next;
        if (slow == fast) return true;
    }
    return false;
}

// Builds the list and links its last node to the node at index loopTo (-1: no loop).
static ListNode? WithLoop(int[] values, int loopTo)
{
    var nodes = values.Select(v => new ListNode(v)).ToArray();
    for (var i = 0; i + 1 < nodes.Length; i++) nodes[i].Next = nodes[i + 1];
    if (loopTo >= 0) nodes[^1].Next = nodes[loopTo];
    return nodes.Length > 0 ? nodes[0] : null;
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
