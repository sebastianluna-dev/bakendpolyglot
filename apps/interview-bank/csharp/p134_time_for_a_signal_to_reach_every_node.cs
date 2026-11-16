// 134 · Time for a Signal to Reach Every Node
Check(SignalTime(4, [[0, 1, 4], [0, 2, 1], [2, 1, 2], [1, 3, 1], [2, 3, 6]], 0) == 4);
Check(SignalTime(3, [[0, 1, 2], [2, 1, 1]], 0) == -1);
Check(SignalTime(1, [], 0) == 0);
Check(SignalTime(2, [[0, 1, 5], [0, 1, 2]], 0) == 2);
Check(SignalTime(2, [[0, 1, 3]], 1) == -1);
Check(SignalTime(2, [[1, 0, 3]], 1) == 3);
Check(SignalTime(3, [[0, 1, 1], [1, 2, 1], [2, 0, 1]], 1) == 2);
Check(SignalTime(3, [[0, 1, 0], [1, 2, 0]], 0) == 0);
Console.WriteLine("134 ok");

static int SignalTime(int n, int[][] links, int source)
{
    var graph = new List<(int To, int Ms)>[n];
    for (var i = 0; i < n; i++) graph[i] = [];
    foreach (var link in links) graph[link[0]].Add((link[1], link[2]));
    var dist = new int[n];
    Array.Fill(dist, int.MaxValue);
    dist[source] = 0;
    var heap = new PriorityQueue<int, int>(); // node, with its distance as the priority
    heap.Enqueue(source, 0);
    while (heap.TryDequeue(out var node, out var d))
    {
        if (d > dist[node]) continue; // stale: a shorter path was found after this was queued
        foreach (var (next, ms) in graph[node])
        {
            if (d + ms < dist[next])
            {
                dist[next] = d + ms;
                heap.Enqueue(next, d + ms);
            }
        }
    }
    var slowest = dist.Max();
    return slowest == int.MaxValue ? -1 : slowest;
}

static void Check(bool ok)
{
    if (!ok) throw new Exception("test failed");
}
