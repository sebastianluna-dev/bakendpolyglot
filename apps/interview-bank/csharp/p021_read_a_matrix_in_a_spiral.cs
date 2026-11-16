// 021 · Read a Matrix in a Spiral
Check(SpiralOrder([[1, 2, 3], [8, 9, 4], [7, 6, 5]]).SequenceEqual([1, 2, 3, 4, 5, 6, 7, 8, 9]));
Check(SpiralOrder([[10, 11, 12, 13], [20, 21, 22, 23], [30, 31, 32, 33]])
    .SequenceEqual([10, 11, 12, 13, 23, 33, 32, 31, 30, 20, 21, 22]));
Check(SpiralOrder([]).Count == 0);
Check(SpiralOrder([[4, 5, 6]]).SequenceEqual([4, 5, 6]));
Check(SpiralOrder([[4], [5], [6]]).SequenceEqual([4, 5, 6]));
Check(SpiralOrder([[1, 2], [3, 4], [5, 6]]).SequenceEqual([1, 2, 4, 6, 5, 3]));
Console.WriteLine("021 ok");

static List<int> SpiralOrder(int[][] grid)
{
    var result = new List<int>();
    if (grid.Length == 0) return result;
    int top = 0, bottom = grid.Length - 1, left = 0, right = grid[0].Length - 1;
    while (top <= bottom && left <= right)
    {
        for (var c = left; c <= right; c++) result.Add(grid[top][c]);
        top++;
        for (var r = top; r <= bottom; r++) result.Add(grid[r][right]);
        right--;
        // after the two steps above, a single row or column may be used up
        if (top <= bottom)
        {
            for (var c = right; c >= left; c--) result.Add(grid[bottom][c]);
            bottom--;
        }
        if (left <= right)
        {
            for (var r = bottom; r >= top; r--) result.Add(grid[r][left]);
            left++;
        }
    }
    return result;
}

static void Check(bool ok)
{
    if (!ok) throw new Exception("test failed");
}
