// 159 · Phone Keypad Words
Check(KeypadWords("23").SequenceEqual(["ad", "ae", "af", "bd", "be", "bf", "cd", "ce", "cf"]));
Check(KeypadWords("7").SequenceEqual(["p", "q", "r", "s"]));
Check(KeypadWords("").Count == 0);
Check(KeypadWords("9").SequenceEqual(["w", "x", "y", "z"]));
var words = KeypadWords("79");
Check(words.Count == 16 && words[0] == "pw" && words[^1] == "sz");
Check(KeypadWords("234").Count == 27);
Console.WriteLine("159 ok");

static List<string> KeypadWords(string digits)
{
    string[] keys = ["", "", "abc", "def", "ghi", "jkl", "mno", "pqrs", "tuv", "wxyz"];
    var result = new List<string>();
    if (digits.Length == 0) return result;
    var path = new System.Text.StringBuilder();
    void Walk(int k)
    {
        if (k == digits.Length)
        {
            result.Add(path.ToString());
            return;
        }
        foreach (var letter in keys[digits[k] - '0'])
        {
            path.Append(letter);
            Walk(k + 1);
            path.Length--;
        }
    }
    Walk(0);
    return result;
}

static void Check(bool ok)
{
    if (!ok) throw new Exception("test failed");
}
