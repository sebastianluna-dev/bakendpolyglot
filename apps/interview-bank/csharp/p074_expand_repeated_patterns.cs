// 074 · Expand Repeated Patterns
using System.Text;

Check(ExpandPatterns("3[ab]2[c]") == "abababcc");
Check(ExpandPatterns("x2[y3[z]]") == "xyzzzyzzz");
Check(ExpandPatterns("") == "");
Check(ExpandPatterns("plain") == "plain");
Check(ExpandPatterns("12[q]") == "qqqqqqqqqqqq");
Check(ExpandPatterns("1[a1[b1[c]]]d") == "abcd");
Check(ExpandPatterns("2[a2[b]c]") == "abbcabbc");
Console.WriteLine("074 ok");

static string ExpandPatterns(string s)
{
    var counts = new Stack<int>();
    var outer = new Stack<StringBuilder>(); // the text built before each open bracket
    var current = new StringBuilder();
    var count = 0;
    foreach (var ch in s)
    {
        if (char.IsAsciiDigit(ch))
        {
            count = count * 10 + (ch - '0');
        }
        else if (ch == '[')
        {
            counts.Push(count);
            outer.Push(current);
            current = new StringBuilder();
            count = 0;
        }
        else if (ch == ']')
        {
            var inner = current.ToString();
            current = outer.Pop();
            for (var k = counts.Pop(); k > 0; k--) current.Append(inner);
        }
        else
        {
            current.Append(ch);
        }
    }
    return current.ToString();
}

static void Check(bool ok)
{
    if (!ok) throw new Exception("test failed");
}
