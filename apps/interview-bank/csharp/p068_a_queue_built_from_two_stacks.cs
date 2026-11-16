// 068 · A Queue Built From Two Stacks
var q = new TwoStackQueue();
Check(q.IsEmpty());
q.Enqueue(1);
q.Enqueue(2);
Check(q.Peek() == 1);
Check(q.Dequeue() == 1);
q.Enqueue(3);
Check(q.Dequeue() == 2);
Check(q.Peek() == 3);
Check(!q.IsEmpty());
Check(q.Dequeue() == 3);
Check(q.IsEmpty());
q.Enqueue(4);
q.Enqueue(5);
Check(q.Dequeue() == 4);
q.Enqueue(6);
Check(q.Dequeue() == 5);
Check(q.Dequeue() == 6);
Check(q.IsEmpty());
Console.WriteLine("068 ok");

static void Check(bool ok)
{
    if (!ok) throw new Exception("test failed");
}

class TwoStackQueue
{
    private readonly Stack<int> inbox = new(); // newest on top
    private readonly Stack<int> outbox = new(); // oldest on top

    public void Enqueue(int x) => inbox.Push(x);

    public int Dequeue()
    {
        Refill();
        return outbox.Pop();
    }

    public int Peek()
    {
        Refill();
        return outbox.Peek();
    }

    public bool IsEmpty() => inbox.Count == 0 && outbox.Count == 0;

    // Only when outbox is empty, or newer items would jump ahead of older ones.
    private void Refill()
    {
        if (outbox.Count > 0) return;
        while (inbox.Count > 0) outbox.Push(inbox.Pop());
    }
}
