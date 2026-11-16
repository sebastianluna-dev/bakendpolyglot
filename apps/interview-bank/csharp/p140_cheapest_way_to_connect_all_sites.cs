// 140 · Cheapest Way to Connect All Sites
Check(CheapestConnection(4, [[0, 1, 3], [1, 2, 1], [2, 3, 4], [0, 2, 2], [1, 3, 5]]) == 7);
Check(CheapestConnection(3, [[0, 1, 5]]) == -1);
Check(CheapestConnection(1, []) == 0);
Check(CheapestConnection(2, []) == -1);
Check(CheapestConnection(2, [[0, 1, 9], [1, 0, 4], [0, 0, 1]]) == 4);
Check(CheapestConnection(5, [[0, 1, 2], [1, 2, 2], [2, 0, 2], [3, 4, 1], [2, 3, 7], [0, 4, 8]]) == 12);
Console.WriteLine("140 ok");

static int CheapestConnection(int n, int[][] cables)
{
    var parent = new int[n];
    var size = new int[n];
    for (var i = 0; i < n; i++)
    {
        parent[i] = i;
        size[i] = 1;
    }
    int total = 0, used = 0;
    foreach (var cable in cables.OrderBy(c => c[2]))
    {
        var ra = Find(parent, cable[0]);
        var rb = Find(parent, cable[1]);
        if (ra == rb) continue; // both ends already connected: this cable would close a loop
        if (size[ra] < size[rb]) (ra, rb) = (rb, ra);
        parent[rb] = ra;
        size[ra] += size[rb];
        total += cable[2];
        used++;
    }
    return used == n - 1 ? total : -1;
}

static int Find(int[] parent, int x)
{
    while (parent[x] != x)
    {
        parent[x] = parent[parent[x]]; // path halving: point x at its grandparent
        x = parent[x];
    }
    return x;
}

static void Check(bool ok)
{
    if (!ok) throw new Exception("test failed");
}
