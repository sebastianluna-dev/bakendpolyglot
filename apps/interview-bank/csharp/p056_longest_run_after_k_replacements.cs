// 056 · Longest Run After k Replacements
Check(LongestRunAfterReplacements("ABAACBA", 1) == 4);
Check(LongestRunAfterReplacements("XYXY", 2) == 4);
Check(LongestRunAfterReplacements("", 0) == 0);
Check(LongestRunAfterReplacements("AAAA", 0) == 4);
Check(LongestRunAfterReplacements("ABCD", 0) == 1);
Check(LongestRunAfterReplacements("AB", 5) == 2);
Check(LongestRunAfterReplacements("BAAABBBBA", 2) == 6);
Console.WriteLine("056 ok");

static int LongestRunAfterReplacements(string s, int k)
{
    var counts = new int[26];
    int left = 0, best = 0;
    for (var right = 0; right < s.Length; right++)
    {
        counts[s[right] - 'A']++;
        // Letters to replace = window length minus the most common letter in it.
        while (right - left + 1 - counts.Max() > k)
        {
            counts[s[left] - 'A']--;
            left++;
        }
        best = Math.Max(best, right - left + 1);
    }
    return best;
}

static void Check(bool ok)
{
    if (!ok) throw new Exception("test failed");
}
