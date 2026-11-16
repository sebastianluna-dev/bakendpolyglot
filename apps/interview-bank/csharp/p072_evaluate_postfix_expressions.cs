// 072 · Evaluate Postfix Expressions
Check(EvaluatePostfix(["4", "13", "5", "/", "+"]) == 6);
Check(EvaluatePostfix(["-7", "2", "/", "3", "*"]) == -9);
Check(EvaluatePostfix(["42"]) == 42);
Check(EvaluatePostfix(["8", "2", "/", "3", "-"]) == 1);
Check(EvaluatePostfix(["2", "-5", "/"]) == 0);
Check(EvaluatePostfix(["-9", "-2", "/"]) == 4);
Check(EvaluatePostfix(["5", "1", "2", "+", "4", "*", "+", "3", "-"]) == 14);
Console.WriteLine("072 ok");

static int EvaluatePostfix(string[] tokens)
{
    var stack = new Stack<int>();
    foreach (var token in tokens)
    {
        if (token is "+" or "-" or "*" or "/")
        {
            var b = stack.Pop();
            var a = stack.Pop();
            stack.Push(token switch
            {
                "+" => a + b,
                "-" => a - b,
                "*" => a * b,
                _ => a / b,
            });
        }
        else
        {
            stack.Push(int.Parse(token));
        }
    }
    return stack.Peek();
}

static void Check(bool ok)
{
    if (!ok) throw new Exception("test failed");
}
