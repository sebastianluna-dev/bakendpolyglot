// 178 · Pattern Matching With . and *
Check(MatchesPattern("seat", "se.*t"));
Check(!MatchesPattern("seats", "se.*t"));
Check(MatchesPattern("", "a*b*"));
Check(MatchesPattern("", ""));
Check(!MatchesPattern("a", ""));
Check(!MatchesPattern("", "."));
Check(MatchesPattern("xxy", "z*x*y"));
Check(!MatchesPattern("ac", "a.c"));
Check(MatchesPattern("aaa", "a*a"));
Check(MatchesPattern("bbb", "bc*b*d*b"));
Check(MatchesPattern("b", "a*b"));
Check(MatchesPattern("ab", ".*"));
Check(!MatchesPattern("abcd", "d*"));
Check(!MatchesPattern("aaaaaaaaaaaaaaaaaaab", "a*a*a*a*a*a*a*a*c"));
Console.WriteLine("178 ok");

static bool MatchesPattern(string text, string pattern)
{
    var m = pattern.Length;
    // cur[j]: the first i letters of text match the first j characters of pattern; prev holds row i - 1
    var prev = new bool[m + 1];
    for (var i = 0; i <= text.Length; i++)
    {
        var cur = new bool[m + 1];
        cur[0] = i == 0;
        for (var j = 1; j <= m; j++)
        {
            if (pattern[j - 1] == '*')
            {
                // the starred item matches nothing, or it takes text[i - 1] and may take more
                cur[j] = cur[j - 2] || (i > 0 && Fits(text[i - 1], pattern[j - 2]) && prev[j]);
            }
            else
            {
                cur[j] = i > 0 && Fits(text[i - 1], pattern[j - 1]) && prev[j - 1];
            }
        }
        prev = cur;
    }
    return prev[m];
}

static bool Fits(char c, char p) => p == '.' || p == c;

static void Check(bool ok)
{
    if (!ok) throw new Exception("test failed");
}
