// 172 · Can the Text Be Split Into Words?
Check(CanSplit("bookseller", ["books", "book", "seller"]));
Check(!CanSplit("trucking", ["truck", "king", "in"]));
Check(CanSplit("", ["a"]));
Check(!CanSplit("abcd", []));
Check(CanSplit("seatseat", ["seat"]));
Check(CanSplit("aaaaaaa", ["aaaa", "aaa"]));
Check(!CanSplit("aaaaaaaaab", ["a", "aa"]));
Console.WriteLine("172 ok");

static bool CanSplit(string text, string[] words)
{
    var dictionary = new HashSet<string>(words);
    var longest = 0;
    foreach (var w in words) longest = Math.Max(longest, w.Length);
    var splits = new bool[text.Length + 1]; // splits[i]: the first i letters split into words
    splits[0] = true;
    for (var end = 1; end <= text.Length; end++)
    {
        for (var len = 1; len <= Math.Min(end, longest); len++)
        {
            if (splits[end - len] && dictionary.Contains(text.Substring(end - len, len)))
            {
                splits[end] = true;
                break;
            }
        }
    }
    return splits[text.Length];
}

static void Check(bool ok)
{
    if (!ok) throw new Exception("test failed");
}
