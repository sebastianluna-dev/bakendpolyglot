// 130 · Service Startup Order
Check(StartupOrder(5, [[0, 3], [2, 4], [1, 0]]).SequenceEqual([3, 0, 1, 4, 2]));
Check(StartupOrder(3, [[0, 1], [1, 2], [2, 0]]).Count == 0);
Check(StartupOrder(3, []).SequenceEqual([0, 1, 2]));
Check(StartupOrder(1, []).SequenceEqual([0]));
Check(StartupOrder(2, [[1, 1]]).Count == 0);
Check(StartupOrder(3, [[2, 0], [2, 0], [1, 2]]).SequenceEqual([0, 2, 1]));
Check(StartupOrder(4, [[1, 0], [2, 3], [3, 2]]).Count == 0);
Console.WriteLine("130 ok");

static List<int> StartupOrder(int n, int[][] dependencies)
{
    var dependents = new List<int>[n];
    for (var i = 0; i < n; i++) dependents[i] = [];
    var waiting = new int[n]; // dependencies not yet started
    foreach (var d in dependencies)
    {
        dependents[d[1]].Add(d[0]);
        waiting[d[0]]++;
    }
    var ready = new PriorityQueue<int, int>();
    for (var s = 0; s < n; s++)
    {
        if (waiting[s] == 0) ready.Enqueue(s, s);
    }
    var order = new List<int>();
    while (ready.TryDequeue(out var service, out _))
    {
        order.Add(service);
        foreach (var d in dependents[service])
        {
            if (--waiting[d] == 0) ready.Enqueue(d, d);
        }
    }
    return order.Count == n ? order : [];
}

static void Check(bool ok)
{
    if (!ok) throw new Exception("test failed");
}
