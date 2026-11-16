// 003 · Same Letters, Different Order
Check(SameLetters("dusty", "study"));
Check(!SameLetters("aab", "abb"));
Check(!SameLetters("loan", "loans"));
Check(SameLetters("abc", "abc"));
Check(SameLetters("", ""));
Console.WriteLine("003 ok");

static bool SameLetters(string a, string b)
{
    if (a.Length != b.Length) return false;
    var counts = new int[26];
    for (var i = 0; i < a.Length; i++)
    {
        counts[a[i] - 'a']++;
        counts[b[i] - 'a']--;
    }
    return counts.All(c => c == 0);
}

static void Check(bool ok)
{
    if (!ok) throw new Exception("test failed");
}
