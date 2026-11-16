// 032 · Run-Length Encode a String
using System.Text;

Check(RunLengthEncode("aaabccdddd") == "a3b1c2d4");
Check(RunLengthEncode("abc") == "a1b1c1");
Check(RunLengthEncode("") == "");
Check(RunLengthEncode(new string('z', 12)) == "z12");
Check(RunLengthEncode("aAaa") == "a1A1a2");
Check(RunLengthEncode("abb") == "a1b2");
Console.WriteLine("032 ok");

static string RunLengthEncode(string s)
{
    var result = new StringBuilder();
    var i = 0;
    while (i < s.Length)
    {
        var j = i;
        while (j < s.Length && s[j] == s[i]) j++;
        result.Append(s[i]).Append(j - i);
        i = j;
    }
    return result.ToString();
}

static void Check(bool ok)
{
    if (!ok) throw new Exception("test failed");
}
