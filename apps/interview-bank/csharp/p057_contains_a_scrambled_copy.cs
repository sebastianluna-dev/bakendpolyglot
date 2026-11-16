// 057 · Contains a Scrambled Copy
Check(ContainsScrambled("loan", "renewalnoted"));
Check(!ContainsScrambled("seat", "stage"));
Check(!ContainsScrambled("abc", "ab"));
Check(ContainsScrambled("ab", "ab"));
Check(ContainsScrambled("aab", "baxaba"));
Check(!ContainsScrambled("aab", "abbb"));
Check(ContainsScrambled("z", "abcz"));
Console.WriteLine("057 ok");

static bool ContainsScrambled(string pattern, string text)
{
    var m = pattern.Length;
    if (m > text.Length) return false;
    var need = new int[26];
    var have = new int[26];
    for (var i = 0; i < m; i++)
    {
        need[pattern[i] - 'a']++;
        have[text[i] - 'a']++;
    }
    if (need.SequenceEqual(have)) return true;
    for (var i = m; i < text.Length; i++)
    {
        have[text[i] - 'a']++; // letter entering the window
        have[text[i - m] - 'a']--; // letter leaving it
        if (need.SequenceEqual(have)) return true;
    }
    return false;
}

static void Check(bool ok)
{
    if (!ok) throw new Exception("test failed");
}
