// 157 · Every Balanced Bracket String
Check(BalancedBrackets(3).SequenceEqual(["((()))", "(()())", "(())()", "()(())", "()()()"]));
Check(BalancedBrackets(1).SequenceEqual(["()"]));
Check(BalancedBrackets(0).SequenceEqual([""]));
Check(BalancedBrackets(2).SequenceEqual(["(())", "()()"]));
Check(BalancedBrackets(4).Count == 14);
Console.WriteLine("157 ok");

static List<string> BalancedBrackets(int n)
{
    var result = new List<string>();
    var path = new System.Text.StringBuilder();
    void Walk(int opened, int closed)
    {
        if (closed == n)
        {
            result.Add(path.ToString());
            return;
        }
        if (opened < n)
        {
            path.Append('(');
            Walk(opened + 1, closed);
            path.Length--;
        }
        if (closed < opened)
        {
            path.Append(')');
            Walk(opened, closed + 1);
            path.Length--;
        }
    }
    Walk(0, 0);
    return result;
}

static void Check(bool ok)
{
    if (!ok) throw new Exception("test failed");
}
