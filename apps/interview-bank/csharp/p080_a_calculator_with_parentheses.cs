// 080 · A Calculator With Parentheses
Check(Calculate("7 - (3 - (1 + 2))") == 7);
Check(Calculate("-(4 - (-2 + 9)) + 10") == 13);
Check(Calculate("12") == 12);
Check(Calculate(" 1 + 2 ") == 3);
Check(Calculate("-5") == -5);
Check(Calculate("(((3)))") == 3);
Check(Calculate("10 - 2 - 3") == 5);
Check(Calculate("1 - (2 + 3)") == -4);
Check(Calculate("-(-(-1))") == -1);
Check(Calculate("2147483647") == 2147483647);
Console.WriteLine("080 ok");

static int Calculate(string s)
{
    int result = 0, sign = 1, number = 0;
    var stack = new Stack<int>(); // for each open bracket: the result before it, then the sign in front of it
    foreach (var ch in s)
    {
        if (char.IsAsciiDigit(ch))
        {
            number = number * 10 + (ch - '0');
        }
        else if (ch is '+' or '-')
        {
            result += sign * number;
            number = 0;
            sign = ch == '+' ? 1 : -1;
        }
        else if (ch == '(')
        {
            stack.Push(result);
            stack.Push(sign);
            result = 0;
            sign = 1;
        }
        else if (ch == ')')
        {
            result += sign * number;
            number = 0;
            var outerSign = stack.Pop();
            result = stack.Pop() + outerSign * result;
        }
    }
    return result + sign * number;
}

static void Check(bool ok)
{
    if (!ok) throw new Exception("test failed");
}
