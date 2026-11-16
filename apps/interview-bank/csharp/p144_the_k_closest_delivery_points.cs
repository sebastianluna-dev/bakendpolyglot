// 144 · The k Closest Delivery Points
Check(Same(KClosest([[3, 4], [1, -2], [-2, 1], [0, 5], [2, 2]], 3), [[-2, 1], [1, -2], [2, 2]]));
Check(Same(KClosest([[2, 0], [0, 2], [0, -2], [-2, 0]], 2), [[-2, 0], [0, -2]]));
Check(KClosest([[5, 5], [1, 1]], 0).Length == 0);
Check(Same(KClosest([[6, 1], [-1, -1], [0, 0]], 3), [[0, 0], [-1, -1], [6, 1]]));
Check(Same(KClosest([[1, 1], [0, 3], [1, 1]], 2), [[1, 1], [1, 1]]));
Check(Same(KClosest([[10000, -10000], [-10000, 10000]], 1), [[-10000, 10000]]));
Console.WriteLine("144 ok");

static int[][] KClosest(int[][] points, int k)
{
    // priority (distance², x, y), largest first: the farthest kept point is on top
    var heap = new PriorityQueue<int[], (int, int, int)>(
        Comparer<(int, int, int)>.Create((a, b) => b.CompareTo(a)));
    foreach (var p in points)
    {
        heap.Enqueue(p, (p[0] * p[0] + p[1] * p[1], p[0], p[1]));
        if (heap.Count > k) heap.Dequeue();
    }
    var result = new int[heap.Count][];
    for (var i = result.Length - 1; i >= 0; i--) result[i] = heap.Dequeue();
    return result;
}

static bool Same(int[][] got, int[][] want) =>
    got.Length == want.Length && got.Zip(want).All(pair => pair.First.SequenceEqual(pair.Second));

static void Check(bool ok)
{
    if (!ok) throw new Exception("test failed");
}
