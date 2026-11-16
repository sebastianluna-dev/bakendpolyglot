// 133 · Count Network Segments
Check(CountSegments(5, [[0, 1], [1, 2], [3, 4]]) == 2);
Check(CountSegments(6, [[0, 1], [2, 3], [1, 0]]) == 4);
Check(CountSegments(1, []) == 1);
Check(CountSegments(3, []) == 3);
Check(CountSegments(3, [[0, 1], [1, 2], [2, 0]]) == 1);
Check(CountSegments(2, [[1, 1]]) == 2);
Check(CountSegments(6, [[0, 1], [2, 3], [4, 5], [1, 3], [5, 0]]) == 1);
Console.WriteLine("133 ok");

static int CountSegments(int n, int[][] links)
{
    var parent = new int[n];
    var size = new int[n];
    for (var i = 0; i < n; i++)
    {
        parent[i] = i;
        size[i] = 1;
    }
    var segments = n;
    foreach (var link in links)
    {
        var ra = Find(parent, link[0]);
        var rb = Find(parent, link[1]);
        if (ra == rb) continue;
        if (size[ra] < size[rb]) (ra, rb) = (rb, ra);
        parent[rb] = ra; // the smaller tree goes under the larger one
        size[ra] += size[rb];
        segments--;
    }
    return segments;
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
