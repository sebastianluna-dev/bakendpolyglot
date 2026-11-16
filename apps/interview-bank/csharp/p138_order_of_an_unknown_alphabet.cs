// 138 · Order of an Unknown Alphabet
using System.Text;

Check(AlphabetOrder(["cab", "cad", "ab", "ba", "bd"]) == "cabd");
Check(AlphabetOrder(["ba", "bc", "ad"]) == "bacd");
Check(AlphabetOrder(["abc", "ab"]) == "");
Check(AlphabetOrder(["a", "b", "a"]) == "");
Check(AlphabetOrder(["a", "b", "c", "a"]) == "");
Check(AlphabetOrder(["zyx"]) == "xyz");
Check(AlphabetOrder(["ab", "ab"]) == "ab");
Check(AlphabetOrder(["ab", "abc"]) == "abc");
Check(AlphabetOrder(["x", "z", "y"]) == "xzy");
Console.WriteLine("138 ok");

static string AlphabetOrder(string[] words)
{
    var present = new bool[26];
    foreach (var w in words)
        foreach (var ch in w) present[ch - 'a'] = true;
    var before = new bool[26, 26]; // before[x, y]: x comes before y
    var waiting = new int[26]; // letters that must come before this one and are not placed yet
    for (var i = 0; i + 1 < words.Length; i++)
    {
        var (a, b) = (words[i], words[i + 1]);
        var j = 0;
        while (j < a.Length && j < b.Length && a[j] == b[j]) j++;
        if (j == a.Length || j == b.Length)
        {
            if (a.Length > b.Length) return ""; // a word sorted before its own prefix
            continue;
        }
        int x = a[j] - 'a', y = b[j] - 'a';
        if (!before[x, y])
        {
            before[x, y] = true;
            waiting[y]++;
        }
    }
    var placed = new bool[26];
    var order = new StringBuilder();
    while (true)
    {
        var x = 0;
        while (x < 26 && !(present[x] && !placed[x] && waiting[x] == 0)) x++;
        if (x == 26) break;
        placed[x] = true;
        order.Append((char)('a' + x));
        for (var y = 0; y < 26; y++)
            if (before[x, y]) waiting[y]--;
    }
    return order.Length == present.Count(p => p) ? order.ToString() : ""; // shorter means a cycle
}

static void Check(bool ok)
{
    if (!ok) throw new Exception("test failed");
}
