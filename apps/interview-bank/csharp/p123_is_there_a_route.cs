// 123 · Is There a Route?
int[][] roads = [[0, 1], [1, 3], [3, 2], [4, 5]];
Check(HasRoute(6, roads, 0, 2));
Check(!HasRoute(6, roads, 0, 5));
Check(HasRoute(1, [], 0, 0));
Check(!HasRoute(3, [], 0, 2));
Check(HasRoute(2, [[1, 0]], 0, 1));
Check(HasRoute(4, [[0, 1], [1, 0], [1, 2], [2, 0], [2, 3]], 3, 0));
Console.WriteLine("123 ok");

static bool HasRoute(int n, int[][] edges, int source, int target)
{
    var graph = new List<int>[n];
    for (var i = 0; i < n; i++) graph[i] = [];
    foreach (var e in edges)
    {
        graph[e[0]].Add(e[1]);
        graph[e[1]].Add(e[0]);
    }
    var seen = new bool[n];
    seen[source] = true;
    var queue = new Queue<int>();
    queue.Enqueue(source);
    while (queue.Count > 0)
    {
        var node = queue.Dequeue();
        if (node == target) return true;
        foreach (var next in graph[node])
        {
            if (seen[next]) continue;
            seen[next] = true; // mark when queued, so no node is queued twice
            queue.Enqueue(next);
        }
    }
    return false;
}

static void Check(bool ok)
{
    if (!ok) throw new Exception("test failed");
}
