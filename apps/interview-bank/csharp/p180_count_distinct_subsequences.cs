// 180 · Count Distinct Subsequences
Check(CountSubsequences("banana", "ban") == 3);
Check(CountSubsequences("seatseat", "sat") == 4);
Check(CountSubsequences("abc", "abcd") == 0);
Check(CountSubsequences("abc", "") == 1);
Check(CountSubsequences("", "a") == 0);
Check(CountSubsequences("", "") == 1);
Check(CountSubsequences("aaaa", "aa") == 6);
Check(CountSubsequences("a", "aa") == 0);
Check(CountSubsequences(new string('a', 1000), new string('a', 500)) == 159835829);
Console.WriteLine("180 ok");

static int CountSubsequences(string s, string t)
{
    const int Mod = 1_000_000_007;
    var m = t.Length;
    var ways = new int[m + 1]; // ways[j]: ways to spell the first j letters of t so far
    ways[0] = 1;
    foreach (var c in s)
    {
        for (var j = m; j >= 1; j--) // downwards, so this letter of s is used at most once
        {
            if (t[j - 1] == c) ways[j] = (ways[j] + ways[j - 1]) % Mod;
        }
    }
    return ways[m];
}

static void Check(bool ok)
{
    if (!ok) throw new Exception("test failed");
}
