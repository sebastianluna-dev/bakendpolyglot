// 045 · Shortest Palindrome by Prepending
Check(ShortestPalindrome("abacd") == "dcabacd");
Check(ShortestPalindrome("stats") == "stats");
Check(ShortestPalindrome("") == "");
Check(ShortestPalindrome("a") == "a");
Check(ShortestPalindrome("ab") == "bab");
Check(ShortestPalindrome("aabba") == "abbaabba");
Check(ShortestPalindrome("aaa") == "aaa");
Console.WriteLine("045 ok");

static string ShortestPalindrome(string s)
{
    var chars = s.ToCharArray();
    Array.Reverse(chars);
    var reversed = new string(chars);
    var t = s + "#" + reversed; // the '#' stops a match from running past the end of s
    var fail = new int[t.Length];
    var k = 0;
    for (var i = 1; i < t.Length; i++)
    {
        while (k > 0 && t[i] != t[k]) k = fail[k - 1];
        if (t[i] == t[k]) k++;
        fail[i] = k;
    }
    var keep = fail[t.Length - 1]; // length of the longest palindromic prefix of s
    return reversed[..(s.Length - keep)] + s;
}

static void Check(bool ok)
{
    if (!ok) throw new Exception("test failed");
}
