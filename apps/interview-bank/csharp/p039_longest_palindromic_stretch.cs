// 039 · Longest Palindromic Stretch
Check(LongestPalindrome("xabbaracecarz") == "racecar");
Check(LongestPalindrome("abcde") == "a");
Check(LongestPalindrome("xyzzyabba") == "yzzy");
Check(LongestPalindrome("banana") == "anana");
Check(LongestPalindrome("abacab") == "bacab");
Check(LongestPalindrome("aaaa") == "aaaa");
Check(LongestPalindrome("a") == "a");
Check(LongestPalindrome("") == "");
Console.WriteLine("039 ok");

static string LongestPalindrome(string s)
{
    int bestStart = 0, bestLength = 0;
    for (var i = 0; i < s.Length; i++)
    {
        foreach (var length in new[] { Expand(s, i, i), Expand(s, i, i + 1) })
        {
            if (length > bestLength) // strictly longer, so the first stretch wins a tie
            {
                bestLength = length;
                bestStart = i - (length - 1) / 2;
            }
        }
    }
    return s.Substring(bestStart, bestLength);
}

static int Expand(string s, int lo, int hi)
{
    while (lo >= 0 && hi < s.Length && s[lo] == s[hi])
    {
        lo--;
        hi++;
    }
    return hi - lo - 1;
}

static void Check(bool ok)
{
    if (!ok) throw new Exception("test failed");
}
