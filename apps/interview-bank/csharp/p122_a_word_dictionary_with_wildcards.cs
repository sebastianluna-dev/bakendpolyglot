// 122 · A Word Dictionary With Wildcards
var d = new WordDictionary();
d.AddWord("loan");
d.AddWord("lean");
Check(d.Search("l.an"));
Check(!d.Search("lo"));
Check(d.Search("l..."));
d.AddWord("load");
Check(d.Search("loan"));
Check(d.Search("lo.d"));
Check(!d.Search("le.d"));
Check(!d.Search("loans"));
Check(d.Search("...."));
Check(!d.Search("..."));
Check(!d.Search("x..."));

var e = new WordDictionary();
Check(!e.Search("."));
e.AddWord("a");
Check(e.Search("."));
Check(e.Search("a"));
Check(!e.Search("b"));
e.AddWord("a");
Check(e.Search("a"));
Console.WriteLine("122 ok");

static void Check(bool ok)
{
    if (!ok) throw new Exception("test failed");
}

class TrieNode
{
    public TrieNode?[] Children = new TrieNode?[26];
    public bool IsWord;
}

class WordDictionary
{
    private readonly TrieNode root = new();

    public void AddWord(string word)
    {
        var node = root;
        foreach (var ch in word)
        {
            node = node.Children[ch - 'a'] ??= new TrieNode();
        }
        node.IsWord = true;
    }

    public bool Search(string pattern) => Match(root, pattern, 0);

    private static bool Match(TrieNode node, string pattern, int i)
    {
        if (i == pattern.Length) return node.IsWord;
        if (pattern[i] == '.')
        {
            return node.Children.Any(child => child != null && Match(child, pattern, i + 1));
        }
        var next = node.Children[pattern[i] - 'a'];
        return next != null && Match(next, pattern, i + 1);
    }
}
