// 160 · Split Into Palindromes
Check(Same(PalindromeSplits("aab"), [["a", "a", "b"], ["aa", "b"]]));
Check(Same(PalindromeSplits("noon"), [["n", "o", "o", "n"], ["n", "oo", "n"], ["noon"]]));
Check(Same(PalindromeSplits("a"), [["a"]]));
Check(Same(PalindromeSplits("ab"), [["a", "b"]]));
Check(Same(PalindromeSplits("aaa"), [["a", "a", "a"], ["a", "aa"], ["aa", "a"], ["aaa"]]));
Check(PalindromeSplits("racecar")[^1].SequenceEqual(["racecar"]));
Console.WriteLine("160 ok");

static List<string[]> PalindromeSplits(string s)
{
    var result = new List<string[]>();
    var path = new List<string>();
    bool IsPalindrome(int lo, int hi)
    {
        while (lo < hi)
        {
            if (s[lo] != s[hi]) return false;
            lo++;
            hi--;
        }
        return true;
    }
    void Walk(int start)
    {
        if (start == s.Length)
        {
            result.Add(path.ToArray());
            return;
        }
        for (var end = start; end < s.Length; end++)
        {
            if (!IsPalindrome(start, end)) continue;
            path.Add(s[start..(end + 1)]);
            Walk(end + 1);
            path.RemoveAt(path.Count - 1);
        }
    }
    Walk(0);
    return result;
}

static bool Same(List<string[]> got, string[][] want) =>
    got.Count == want.Length && got.Zip(want).All(p => p.First.SequenceEqual(p.Second));

static void Check(bool ok)
{
    if (!ok) throw new Exception("test failed");
}
