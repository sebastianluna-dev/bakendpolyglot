// 030 · Letter-for-Letter Mapping
Check(CanMapLetters("deed", "noon"));
Check(!CanMapLetters("bell", "cool"));
Check(CanMapLetters("", ""));
Check(!CanMapLetters("a", "bc"));
Check(CanMapLetters("abc", "bca"));
Check(!CanMapLetters("ab", "aa"));
Check(!CanMapLetters("aa", "ab"));
Check(!CanMapLetters("tab", "tat"));
Console.WriteLine("030 ok");

static bool CanMapLetters(string source, string target)
{
    if (source.Length != target.Length) return false;
    var forward = new Dictionary<char, char>();
    var backward = new Dictionary<char, char>(); // stops two letters mapping to the same one
    for (var i = 0; i < source.Length; i++)
    {
        char a = source[i], b = target[i];
        if (forward.GetValueOrDefault(a, b) != b || backward.GetValueOrDefault(b, a) != a) return false;
        forward[a] = b;
        backward[b] = a;
    }
    return true;
}

static void Check(bool ok)
{
    if (!ok) throw new Exception("test failed");
}
