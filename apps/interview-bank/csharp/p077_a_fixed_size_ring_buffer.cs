// 077 · A Fixed-Size Ring Buffer
var b = new RingBuffer(3);
Check(b.IsEmpty());
Check(b.Enqueue(5));
Check(b.Enqueue(8));
Check(b.Enqueue(2));
Check(!b.Enqueue(9));
Check(b.IsFull());
Check(b.Rear() == 2);
Check(b.Dequeue() == 5);
Check(b.Enqueue(9));
Check(b.Front() == 8);
Check(b.Rear() == 9);
Check(b.Dequeue() == 8);
Check(b.Dequeue() == 2);
Check(b.Dequeue() == 9);
Check(b.Dequeue() == -1);
Check(b.IsEmpty());
Check(b.Front() == -1);
Check(b.Rear() == -1);

var one = new RingBuffer(1);
Check(one.Enqueue(0));
Check(one.IsFull());
Check(!one.Enqueue(1));
Check(one.Front() == 0);
Check(one.Rear() == 0);
Check(one.Dequeue() == 0);
Check(one.IsEmpty());
Console.WriteLine("077 ok");

static void Check(bool ok)
{
    if (!ok) throw new Exception("test failed");
}

class RingBuffer(int capacity)
{
    private readonly int[] items = new int[capacity];
    private int head; // index of the oldest value
    private int count;

    public bool Enqueue(int value)
    {
        if (IsFull()) return false;
        items[(head + count) % items.Length] = value;
        count++;
        return true;
    }

    public int Dequeue()
    {
        if (IsEmpty()) return -1;
        var value = items[head];
        head = (head + 1) % items.Length;
        count--;
        return value;
    }

    public int Front() => IsEmpty() ? -1 : items[head];

    public int Rear() => IsEmpty() ? -1 : items[(head + count - 1) % items.Length];

    public bool IsEmpty() => count == 0;

    public bool IsFull() => count == items.Length;
}
