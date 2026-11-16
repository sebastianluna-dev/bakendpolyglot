// 174 · Longest Common Subsequence
Check(LongestCommonSubsequence("parcel", "place") == 4);
Check(LongestCommonSubsequence("seat", "lock") == 0);
Check(LongestCommonSubsequence("", "abc") == 0);
Check(LongestCommonSubsequence("abc", "") == 0);
Check(LongestCommonSubsequence("member", "member") == 6);
Check(LongestCommonSubsequence("aaaa", "aa") == 2);
Check(LongestCommonSubsequence("delivery", "relived") == 5);
Console.WriteLine("174 ok");

static int LongestCommonSubsequence(string a, string b)
{
    var m = b.Length;
    // cur[j]: answer for the first i letters of a and the first j of b; prev holds row i - 1
    var prev = new int[m + 1];
    for (var i = 1; i <= a.Length; i++)
    {
        var cur = new int[m + 1];
        for (var j = 1; j <= m; j++)
        {
            if (a[i - 1] == b[j - 1]) cur[j] = prev[j - 1] + 1;
            else cur[j] = Math.Max(prev[j], cur[j - 1]);
        }
        prev = cur;
    }
    return prev[m];
}

static void Check(bool ok)
{
    if (!ok) throw new Exception("test failed");
}
