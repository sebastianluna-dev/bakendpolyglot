// 073 · Days Until a Warmer Day
Check(DaysUntilWarmer([18, 16, 17, 21, 20, 20, 23]).SequenceEqual([3, 1, 1, 3, 2, 1, 0]));
Check(DaysUntilWarmer([30, 25, 20]).SequenceEqual([0, 0, 0]));
Check(DaysUntilWarmer([]).Length == 0);
Check(DaysUntilWarmer([12]).SequenceEqual([0]));
Check(DaysUntilWarmer([5, 5, 5]).SequenceEqual([0, 0, 0]));
Check(DaysUntilWarmer([-3, -1, 0, 2]).SequenceEqual([1, 1, 1, 0]));
Console.WriteLine("073 ok");

static int[] DaysUntilWarmer(int[] temps)
{
    var wait = new int[temps.Length];
    var stack = new Stack<int>(); // indices still waiting, their temperatures never rising from bottom to top
    for (var i = 0; i < temps.Length; i++)
    {
        while (stack.Count > 0 && temps[stack.Peek()] < temps[i])
        {
            var j = stack.Pop();
            wait[j] = i - j;
        }
        stack.Push(i);
    }
    return wait;
}

static void Check(bool ok)
{
    if (!ok) throw new Exception("test failed");
}
