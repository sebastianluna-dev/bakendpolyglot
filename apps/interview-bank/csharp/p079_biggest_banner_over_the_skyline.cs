// 079 · Biggest Banner Over the Skyline
Check(BiggestBanner([3, 1, 4, 5, 4, 2]) == 12);
Check(BiggestBanner([2, 4, 2]) == 6);
Check(BiggestBanner([]) == 0);
Check(BiggestBanner([7]) == 7);
Check(BiggestBanner([1, 2, 3, 4, 5]) == 9);
Check(BiggestBanner([5, 4, 3, 2, 1]) == 9);
Check(BiggestBanner([2, 1, 2]) == 3);
Check(BiggestBanner([0, 0]) == 0);
Check(BiggestBanner([4, 0, 4]) == 4);
Console.WriteLine("079 ok");

static int BiggestBanner(int[] heights)
{
    var best = 0;
    var stack = new Stack<int>(); // indices of rising heights
    for (var i = 0; i <= heights.Length; i++)
    {
        var h = i < heights.Length ? heights[i] : 0; // a zero at the end empties the stack
        while (stack.Count > 0 && heights[stack.Peek()] >= h)
        {
            var top = stack.Pop();
            var left = stack.Count > 0 ? stack.Peek() : -1;
            best = Math.Max(best, heights[top] * (i - left - 1));
        }
        stack.Push(i);
    }
    return best;
}

static void Check(bool ok)
{
    if (!ok) throw new Exception("test failed");
}
