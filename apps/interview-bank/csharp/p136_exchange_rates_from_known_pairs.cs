// 136 · Exchange Rates From Known Pairs
Check(Near(
    ExchangeRates(
        [["EUR", "USD"], ["USD", "MXN"]],
        [1.25, 20.0],
        [["EUR", "MXN"], ["MXN", "EUR"], ["USD", "USD"], ["EUR", "JPY"], ["JPY", "JPY"]]),
    [25.0, 0.04, 1.0, -1.0, -1.0]));
Check(Near(
    ExchangeRates(
        [["GBP", "EUR"], ["CHF", "SEK"], ["SEK", "NOK"]],
        [1.2, 12.0, 0.5],
        [["EUR", "GBP"], ["CHF", "NOK"], ["NOK", "CHF"], ["GBP", "NOK"]]),
    [0.8333333333, 6.0, 0.1666666667, -1.0]));
Check(Near(ExchangeRates([["EUR", "USD"]], [1.25], []), []));
Check(Near(
    ExchangeRates([["USD", "CAD"], ["CAD", "AUD"], ["USD", "AUD"]], [1.5, 1.2, 1.8], [["AUD", "USD"], ["CAD", "CAD"]]),
    [0.5555555556, 1.0]));
Console.WriteLine("136 ok");

static double[] ExchangeRates(string[][] pairs, double[] rates, string[][] queries)
{
    var graph = new Dictionary<string, List<(string To, double Rate)>>();
    for (var i = 0; i < pairs.Length; i++)
    {
        var (from, to) = (pairs[i][0], pairs[i][1]);
        if (!graph.ContainsKey(from)) graph[from] = [];
        if (!graph.ContainsKey(to)) graph[to] = [];
        graph[from].Add((to, rates[i]));
        graph[to].Add((from, 1 / rates[i]));
    }
    var group = new Dictionary<string, int>();
    var value = new Dictionary<string, double>(); // units of this currency worth one unit of its group's first
    foreach (var start in graph.Keys)
    {
        if (group.ContainsKey(start)) continue;
        var id = group.Count;
        group[start] = id;
        value[start] = 1;
        var queue = new Queue<string>();
        queue.Enqueue(start);
        while (queue.TryDequeue(out var cur))
        {
            foreach (var (next, rate) in graph[cur])
            {
                if (group.ContainsKey(next)) continue;
                group[next] = id;
                value[next] = value[cur] * rate;
                queue.Enqueue(next);
            }
        }
    }
    var answers = new double[queries.Length];
    for (var i = 0; i < queries.Length; i++)
    {
        var (x, y) = (queries[i][0], queries[i][1]);
        var known = group.TryGetValue(x, out var gx) && group.TryGetValue(y, out var gy) && gx == gy;
        answers[i] = known ? value[y] / value[x] : -1.0;
    }
    return answers;
}

static bool Near(double[] got, double[] want) =>
    got.Length == want.Length && got.Zip(want).All(p => Math.Abs(p.First - p.Second) < 1e-9);

static void Check(bool ok)
{
    if (!ok) throw new Exception("test failed");
}
