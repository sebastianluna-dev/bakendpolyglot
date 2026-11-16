// 055 · Longest Stretch Without a Repeat
Check(LongestWithoutRepeat("shelfwise") == 7);
Check(LongestWithoutRepeat("aaaa") == 1);
Check(LongestWithoutRepeat("") == 0);
Check(LongestWithoutRepeat("abba") == 2);
Check(LongestWithoutRepeat("dvdf") == 3);
Check(LongestWithoutRepeat("A-1 a-2") == 5);
Console.WriteLine("055 ok");

static int LongestWithoutRepeat(string s)
{
    var last = new Dictionary<char, int>(); // character -> index where it was last seen
    int start = 0, best = 0;
    for (var i = 0; i < s.Length; i++)
    {
        if (last.TryGetValue(s[i], out var prev) && prev >= start) start = prev + 1; // only a repeat inside the window counts
        last[s[i]] = i;
        best = Math.Max(best, i - start + 1);
    }
    return best;
}

static void Check(bool ok)
{
    if (!ok) throw new Exception("test failed");
}
