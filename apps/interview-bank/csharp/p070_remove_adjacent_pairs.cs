// 070 · Remove Adjacent Pairs
using System.Text;

Check(RemovePairs("abccbd") == "ad");
Check(RemovePairs("aabb") == "");
Check(RemovePairs("") == "");
Check(RemovePairs("abc") == "abc");
Check(RemovePairs("aaa") == "a");
Check(RemovePairs("abba") == "");
Check(RemovePairs("mississippi") == "m");
Console.WriteLine("070 ok");

static string RemovePairs(string s)
{
    var kept = new StringBuilder(); // used as a stack: the result so far
    foreach (var ch in s)
    {
        if (kept.Length > 0 && kept[^1] == ch) kept.Length--;
        else kept.Append(ch);
    }
    return kept.ToString();
}

static void Check(bool ok)
{
    if (!ok) throw new Exception("test failed");
}
