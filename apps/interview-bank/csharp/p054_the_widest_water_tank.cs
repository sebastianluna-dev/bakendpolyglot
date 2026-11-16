// 054 · The Widest Water Tank
Check(WidestTank([2, 6, 3, 1, 5, 2]) == 15);
Check(WidestTank([4, 4]) == 4);
Check(WidestTank([7]) == 0);
Check(WidestTank([]) == 0);
Check(WidestTank([1, 2, 3, 4, 5]) == 6);
Check(WidestTank([0, 0, 0]) == 0);
Check(WidestTank([5, 1, 1, 1, 5]) == 20);
Console.WriteLine("054 ok");

static int WidestTank(int[] heights)
{
    int left = 0, right = heights.Length - 1, best = 0;
    while (left < right)
    {
        var area = (right - left) * Math.Min(heights[left], heights[right]);
        best = Math.Max(best, area);
        // The shorter wall limits every tank that keeps it, so move it inward.
        if (heights[left] <= heights[right]) left++;
        else right--;
    }
    return best;
}

static void Check(bool ok)
{
    if (!ok) throw new Exception("test failed");
}
