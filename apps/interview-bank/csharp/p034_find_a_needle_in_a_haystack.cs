// 034 · Find a Needle in a Haystack
Check(FindNeedle("hold-14-seat-14", "14") == 5);
Check(FindNeedle("parcel", "pulse") == -1);
Check(FindNeedle("abc", "c") == 2);
Check(FindNeedle("aaab", "ab") == 2);
Check(FindNeedle("abc", "") == 0);
Check(FindNeedle("", "") == 0);
Check(FindNeedle("", "a") == -1);
Check(FindNeedle("ab", "abc") == -1);
Console.WriteLine("034 ok");

static int FindNeedle(string haystack, string needle)
{
    for (var start = 0; start + needle.Length <= haystack.Length; start++)
    {
        var k = 0;
        while (k < needle.Length && haystack[start + k] == needle[k]) k++;
        if (k == needle.Length) return start;
    }
    return -1;
}

static void Check(bool ok)
{
    if (!ok) throw new Exception("test failed");
}
