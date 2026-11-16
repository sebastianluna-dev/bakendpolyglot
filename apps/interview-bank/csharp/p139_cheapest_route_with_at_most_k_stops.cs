// 139 · Cheapest Route With at Most k Stops
int[][] legs = [[0, 1, 100], [1, 2, 100], [2, 3, 100], [0, 2, 500], [1, 3, 600]];
int[][] triangle = [[0, 1, 1], [1, 2, 1], [0, 2, 5]];
Check(CheapestRoute(4, legs, 0, 3, 1) == 600);
Check(CheapestRoute(4, legs, 0, 3, 2) == 300);
Check(CheapestRoute(4, legs, 0, 3, 0) == -1);
Check(CheapestRoute(4, legs, 0, 3, 10) == 300);
Check(CheapestRoute(3, triangle, 0, 2, 0) == 5);
Check(CheapestRoute(3, triangle, 0, 2, 1) == 2);
Check(CheapestRoute(2, [[0, 1, 7]], 1, 1, 0) == 0);
Check(CheapestRoute(3, [[0, 1, 4]], 0, 2, 5) == -1);
Check(CheapestRoute(2, [[1, 0, 3]], 0, 1, 3) == -1);
Console.WriteLine("139 ok");

static int CheapestRoute(int n, int[][] legs, int source, int target, int k)
{
    var cost = new int[n];
    Array.Fill(cost, int.MaxValue);
    cost[source] = 0;
    for (var round = 0; round <= k; round++)
    {
        var next = (int[])cost.Clone(); // relax from last round's costs only, so each round adds at most one leg
        foreach (var leg in legs)
        {
            int from = leg[0], to = leg[1], price = leg[2];
            if (cost[from] != int.MaxValue && cost[from] + price < next[to]) next[to] = cost[from] + price;
        }
        cost = next;
    }
    return cost[target] == int.MaxValue ? -1 : cost[target];
}

static void Check(bool ok)
{
    if (!ok) throw new Exception("test failed");
}
