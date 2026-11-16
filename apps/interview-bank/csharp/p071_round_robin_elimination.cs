// 071 · Round-Robin Elimination
Check(EliminationOrder(5, 2).SequenceEqual([2, 4, 1, 5, 3]));
Check(EliminationOrder(4, 3).SequenceEqual([3, 2, 4, 1]));
Check(EliminationOrder(1, 7).SequenceEqual([1]));
Check(EliminationOrder(4, 1).SequenceEqual([1, 2, 3, 4]));
Check(EliminationOrder(3, 5).SequenceEqual([2, 3, 1]));
Check(EliminationOrder(6, 6).SequenceEqual([6, 1, 3, 2, 5, 4]));
Console.WriteLine("071 ok");

static int[] EliminationOrder(int n, int k)
{
    var circle = new Queue<int>();
    for (var i = 1; i <= n; i++) circle.Enqueue(i);
    var order = new List<int>();
    while (circle.Count > 0)
    {
        var steps = (k - 1) % circle.Count; // skip whole laps when k is larger than the circle
        for (var s = 0; s < steps; s++) circle.Enqueue(circle.Dequeue());
        order.Add(circle.Dequeue());
    }
    return [.. order];
}

static void Check(bool ok)
{
    if (!ok) throw new Exception("test failed");
}
