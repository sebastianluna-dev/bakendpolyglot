// 015 · The k Most Borrowed Books
Check(MostBorrowed(["dune", "emma", "dune", "ulysses", "emma", "dune", "beloved"], 2).SequenceEqual(["dune", "emma"]));
Check(MostBorrowed(["emma", "beloved", "ulysses", "beloved", "emma"], 2).SequenceEqual(["beloved", "emma"]));
Check(MostBorrowed(["dune"], 3).SequenceEqual(["dune"]));
Check(MostBorrowed([], 1).Count == 0);
Check(MostBorrowed(["c", "b", "a"], 2).SequenceEqual(["a", "b"]));
Console.WriteLine("015 ok");

static List<string> MostBorrowed(string[] loans, int k)
{
    var counts = new Dictionary<string, int>();
    foreach (var title in loans) counts[title] = counts.GetValueOrDefault(title) + 1;
    return counts
        .OrderByDescending(e => e.Value)
        .ThenBy(e => e.Key, StringComparer.Ordinal) // the default comparer is culture-sensitive
        .Take(k)
        .Select(e => e.Key)
        .ToList();
}

static void Check(bool ok)
{
    if (!ok) throw new Exception("test failed");
}
