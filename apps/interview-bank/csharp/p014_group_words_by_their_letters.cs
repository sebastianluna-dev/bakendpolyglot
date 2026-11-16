// 014 · Group Words by Their Letters
Check(Same(GroupByLetters(["listen", "stone", "silent", "notes", "enlist", "tones", "tab"]),
    [["listen", "silent", "enlist"], ["stone", "notes", "tones"], ["tab"]]));
Check(Same(GroupByLetters(["loop", "pool", "polo", "lop"]), [["loop", "pool", "polo"], ["lop"]]));
Check(GroupByLetters([]).Count == 0);
Check(Same(GroupByLetters(["ab", "ba", "ab"]), [["ab", "ba", "ab"]]));
Check(Same(GroupByLetters(["", "a", ""]), [["", ""], ["a"]]));
Console.WriteLine("014 ok");

static List<List<string>> GroupByLetters(string[] words)
{
    var groupOf = new Dictionary<string, int>(); // sorted letters -> index in groups
    var groups = new List<List<string>>();
    foreach (var word in words)
    {
        var letters = word.ToCharArray();
        Array.Sort(letters);
        var key = new string(letters);
        if (!groupOf.TryGetValue(key, out var g))
        {
            g = groups.Count;
            groupOf[key] = g;
            groups.Add([]);
        }
        groups[g].Add(word);
    }
    return groups;
}

static bool Same(List<List<string>> got, string[][] want) =>
    got.Count == want.Length && got.Zip(want).All(p => p.First.SequenceEqual(p.Second));

static void Check(bool ok)
{
    if (!ok) throw new Exception("test failed");
}
