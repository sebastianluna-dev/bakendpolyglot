// 141 · The k-th Largest in a Stream
var board = new KthLargest(3, [40, 75, 20, 90]);
Check(board.Add(50) == 50);
Check(board.Add(10) == 50);
Check(board.Add(80) == 75);
Check(board.Add(95) == 80);

var single = new KthLargest(1, []);
Check(single.Add(-3) == -3);
Check(single.Add(-5) == -3);
Check(single.Add(2) == 2);

var ties = new KthLargest(2, [7]);
Check(ties.Add(7) == 7);
Check(ties.Add(3) == 7);
Check(ties.Add(9) == 7);
Check(ties.Add(8) == 8);

var many = new KthLargest(4, [5, 1, 9, 3, 7, 2]);
Check(many.Add(4) == 4);
Check(many.Add(6) == 5);
Check(many.Add(10) == 6);
Console.WriteLine("141 ok");

static void Check(bool ok)
{
    if (!ok) throw new Exception("test failed");
}

class KthLargest
{
    private readonly int k;
    private readonly PriorityQueue<int, int> heap = new(); // the k largest values so far; the smallest is on top

    public KthLargest(int k, int[] nums)
    {
        this.k = k;
        foreach (var x in nums) Add(x);
    }

    public int Add(int value)
    {
        if (heap.Count < k) heap.Enqueue(value, value);
        else heap.EnqueueDequeue(value, value); // add value, then drop the smallest, in one step
        return heap.Peek();
    }
}
