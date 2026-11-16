// 046 · Is One a Subsequence of the Other?
Check(IsSubsequence("ACE", "ABCDE"));
Check(!IsSubsequence("AEC", "ABCDE"));
Check(IsSubsequence("", "ABC"));
Check(!IsSubsequence("A", ""));
Check(IsSubsequence("AA", "ABA"));
Check(!IsSubsequence("AAB", "ABAC"));
Console.WriteLine("046 ok");

static bool IsSubsequence(string plan, string scans)
{
    int i = 0, j = 0;
    while (i < plan.Length && j < scans.Length)
    {
        if (plan[i] == scans[j]) i++;
        j++;
    }
    return i == plan.Length;
}

static void Check(bool ok)
{
    if (!ok) throw new Exception("test failed");
}
