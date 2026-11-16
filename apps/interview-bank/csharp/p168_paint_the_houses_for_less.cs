// 168 · Paint the Houses for Less
Check(MinPaintCost([[8, 3, 6], [4, 9, 2], [7, 5, 1]]) == 8);
Check(MinPaintCost([[1, 2, 9], [1, 9, 9]]) == 3);
Check(MinPaintCost([]) == 0);
Check(MinPaintCost([[5, 1, 9]]) == 1);
Check(MinPaintCost([[7, 6, 2], [3, 8, 9], [4, 1, 6], [9, 2, 3]]) == 9);
Check(MinPaintCost([[4, 4, 4], [4, 4, 4]]) == 8);
Console.WriteLine("168 ok");

static int MinPaintCost(int[][] costs)
{
    int red = 0, green = 0, blue = 0; // cheapest totals so far, by the colour of the last house
    foreach (var c in costs)
    {
        (red, green, blue) = (c[0] + Math.Min(green, blue), c[1] + Math.Min(red, blue), c[2] + Math.Min(red, green));
    }
    return Math.Min(red, Math.Min(green, blue));
}

static void Check(bool ok)
{
    if (!ok) throw new Exception("test failed");
}
