// 149 · Running Median
var m = new RunningMedian();
m.AddNum(5);
Check(m.FindMedian() == 5.0);
m.AddNum(15);
Check(m.FindMedian() == 10.0);
m.AddNum(1);
Check(m.FindMedian() == 5.0);
m.AddNum(3);
Check(m.FindMedian() == 4.0);

var neg = new RunningMedian();
neg.AddNum(-2);
Check(neg.FindMedian() == -2.0);
neg.AddNum(-2);
Check(neg.FindMedian() == -2.0);
neg.AddNum(7);
Check(neg.FindMedian() == -2.0);
neg.AddNum(8);
Check(neg.FindMedian() == 2.5);

var down = new RunningMedian();
var medians = new List<double>();
foreach (var x in new[] { 9, 8, 7, 6, 5 })
{
    down.AddNum(x);
    medians.Add(down.FindMedian());
}
Check(medians.SequenceEqual([9.0, 8.5, 8.0, 7.5, 7.0]));

var big = new RunningMedian();
big.AddNum(int.MaxValue);
big.AddNum(int.MaxValue);
Check(big.FindMedian() == int.MaxValue);
big.AddNum(int.MinValue);
big.AddNum(int.MinValue);
Check(big.FindMedian() == -0.5);
Console.WriteLine("149 ok");

static void Check(bool ok)
{
    if (!ok) throw new Exception("test failed");
}

class RunningMedian
{
    // the smaller half, largest on top
    private readonly PriorityQueue<int, int> low = new(Comparer<int>.Create((a, b) => b.CompareTo(a)));
    private readonly PriorityQueue<int, int> high = new(); // the larger half, smallest on top

    public void AddNum(int num)
    {
        low.Enqueue(num, num);
        var top = low.Dequeue();
        high.Enqueue(top, top);
        if (high.Count > low.Count)
        {
            top = high.Dequeue();
            low.Enqueue(top, top);
        }
    }

    public double FindMedian()
    {
        if (low.Count > high.Count) return low.Peek();
        return ((double)low.Peek() + high.Peek()) / 2; // double first: the int sum can overflow
    }
}
