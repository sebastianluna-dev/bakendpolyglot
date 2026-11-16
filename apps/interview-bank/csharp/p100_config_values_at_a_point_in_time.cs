// 100 · Config Values at a Point in Time
var h = new ConfigHistory();
Check(h.Get("hold.minutes", 100) == "");
h.Set("hold.minutes", "10", 100);
h.Set("hold.minutes", "15", 250);
h.Set("currency", "EUR", 120);
Check(h.Get("hold.minutes", 99) == "");
Check(h.Get("hold.minutes", 100) == "10");
Check(h.Get("hold.minutes", 249) == "10");
Check(h.Get("hold.minutes", 250) == "15");
Check(h.Get("hold.minutes", 1000) == "15");
Check(h.Get("currency", 119) == "");
Check(h.Get("currency", 500) == "EUR");
Check(h.Get("refunds", 500) == "");
h.Set("hold.minutes", "20", 400);
Check(h.Get("hold.minutes", 399) == "15");
Check(h.Get("hold.minutes", 400) == "20");
Console.WriteLine("100 ok");

static void Check(bool ok)
{
    if (!ok) throw new Exception("test failed");
}

class ConfigHistory
{
    // key -> (timestamp, value), oldest first
    private readonly Dictionary<string, List<(int Timestamp, string Value)>> history = new();

    public void Set(string key, string value, int timestamp)
    {
        if (!history.TryGetValue(key, out var entries))
        {
            entries = [];
            history[key] = entries;
        }
        entries.Add((timestamp, value)); // timestamps only grow, so the list stays sorted
    }

    public string Get(string key, int timestamp)
    {
        if (!history.TryGetValue(key, out var entries)) return "";
        int lo = 0, hi = entries.Count; // looking for the first entry set after timestamp
        while (lo < hi)
        {
            var mid = lo + (hi - lo) / 2;
            if (entries[mid].Timestamp > timestamp) hi = mid;
            else lo = mid + 1;
        }
        return lo == 0 ? "" : entries[lo - 1].Value;
    }
}
