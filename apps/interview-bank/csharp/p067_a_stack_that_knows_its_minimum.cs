// 067 · A Stack That Knows Its Minimum
var s = new MinStack();
s.Push(5);
s.Push(3);
s.Push(7);
Check(s.Min() == 3);
Check(s.Top() == 7);
Check(s.Pop() == 7);
Check(s.Min() == 3);
Check(s.Pop() == 3);
Check(s.Min() == 5);
Check(s.Pop() == 5);

s.Push(4);
s.Push(1);
s.Push(1);
Check(s.Pop() == 1);
Check(s.Min() == 1);
Check(s.Pop() == 1);
Check(s.Min() == 4);
s.Push(-2);
Check(s.Min() == -2);
Check(s.Top() == -2);
Check(s.Pop() == -2);
Check(s.Top() == 4);
Check(s.Min() == 4);
Console.WriteLine("067 ok");

static void Check(bool ok)
{
    if (!ok) throw new Exception("test failed");
}

class MinStack
{
    private readonly Stack<int> values = new();
    private readonly Stack<int> mins = new(); // each entry is the smallest value at or below it

    public void Push(int x)
    {
        values.Push(x);
        mins.Push(mins.Count == 0 ? x : Math.Min(x, mins.Peek()));
    }

    public int Pop()
    {
        mins.Pop();
        return values.Pop();
    }

    public int Top() => values.Peek();

    public int Min() => mins.Peek();
}
