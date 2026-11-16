// 191 · Requests per Endpoint and Status From Access Logs
Check(CountRequests([
    "2026-03-02T09:00:01Z GET /books 200 12",
    "2026-03-02T09:00:02Z POST /loans 201 40",
    "2026-03-02T09:00:03Z GET /books 200 9",
    "2026-03-02T09:00:04Z POST /loans 409 15",
    "2026-03-02T09:00:05Z GET /books 200 11",
    "2026-03-02T09:00:06Z POST /loans 201 38",
]).SequenceEqual(["GET /books 200 3", "POST /loans 201 2", "POST /loans 409 1"]));
Check(CountRequests([
    "2026-03-02T09:01:00Z GET /members/7 404 3",
    "",
    "garbage",
    "2026-03-02T09:01:02Z DELETE /holds/9 204 5",
    "2026-03-02T09:01:03Z GET /members/7 200 4",
]).SequenceEqual(["DELETE /holds/9 204 1", "GET /members/7 200 1", "GET /members/7 404 1"]));
Check(CountRequests([]).Length == 0);
Check(CountRequests([
    "2026-03-02T09:02:00Z GET  /books 200 7",
    "2026-03-02T09:02:01Z GET /books 200 7 ",
    "2026-03-02T09:02:02Z GET /books 200",
]).Length == 0);
Check(CountRequests(["t1 GET /a 500 1", "t2 GET /b 200 1", "t3 GET /a 500 1", "t4 GET /b 200 1", "t5 GET /a 200 1"])
    .SequenceEqual(["GET /a 500 2", "GET /b 200 2", "GET /a 200 1"]));
Console.WriteLine("191 ok");

static string[] CountRequests(string[] lines)
{
    var counts = new Dictionary<string, int>(); // "METHOD PATH STATUS" -> requests
    foreach (var line in lines)
    {
        var fields = line.Split(' ');
        if (fields.Length != 5 || fields.Contains("")) continue;
        var key = $"{fields[1]} {fields[2]} {fields[3]}";
        counts[key] = counts.GetValueOrDefault(key) + 1;
    }
    return counts
        .OrderByDescending(kv => kv.Value)
        .ThenBy(kv => kv.Key, StringComparer.Ordinal) // the default comparer depends on the culture
        .Select(kv => $"{kv.Key} {kv.Value}")
        .ToArray();
}

static void Check(bool ok)
{
    if (!ok) throw new Exception("test failed");
}
