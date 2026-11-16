// 142 · Smash the Heaviest Stones
Check(LastStone([3, 9, 4, 4, 1]) == 1);
Check(LastStone([10, 4, 4, 2]) == 0);
Check(LastStone([6, 6]) == 0);
Check(LastStone([7]) == 7);
Check(LastStone([1, 1, 1]) == 1);
Check(LastStone([2, 2, 3]) == 1);
Check(LastStone([5, 1, 1, 1, 1, 1]) == 0);
Console.WriteLine("142 ok");

static int LastStone(int[] stones)
{
    var heap = new PriorityQueue<int, int>(Comparer<int>.Create((a, b) => b.CompareTo(a))); // heaviest first
    foreach (var s in stones) heap.Enqueue(s, s);
    while (heap.Count > 1)
    {
        var x = heap.Dequeue();
        var y = heap.Dequeue();
        if (x > y) heap.Enqueue(x - y, x - y);
    }
    return heap.Count == 1 ? heap.Peek() : 0;
}

static void Check(bool ok)
{
    if (!ok) throw new Exception("test failed");
}
