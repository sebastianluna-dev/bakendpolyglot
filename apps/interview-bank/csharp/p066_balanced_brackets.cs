// 066 · Balanced Brackets
Check(IsBalanced("lookup(ids[0], {limit: 5})"));
Check(!IsBalanced("([)]"));
Check(IsBalanced(""));
Check(IsBalanced("no brackets"));
Check(!IsBalanced("("));
Check(!IsBalanced(")"));
Check(!IsBalanced("}{"));
Check(!IsBalanced("(("));
Check(IsBalanced("{[()()]}"));
Console.WriteLine("066 ok");

static bool IsBalanced(string s)
{
    var expected = new Stack<char>(); // the closer each open bracket is waiting for
    foreach (var ch in s)
    {
        switch (ch)
        {
            case '(': expected.Push(')'); break;
            case '[': expected.Push(']'); break;
            case '{': expected.Push('}'); break;
            case ')' or ']' or '}':
                if (!expected.TryPop(out var want) || want != ch) return false;
                break;
        }
    }
    return expected.Count == 0;
}

static void Check(bool ok)
{
    if (!ok) throw new Exception("test failed");
}
