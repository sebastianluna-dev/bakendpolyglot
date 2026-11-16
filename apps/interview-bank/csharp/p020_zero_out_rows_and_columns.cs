// 020 · Zero Out Rows and Columns
Check(Same(Zeroed([[1, 2, 3], [4, 0, 6], [7, 8, 9]]), [[1, 0, 3], [0, 0, 0], [7, 0, 9]]));
Check(Same(Zeroed([[0, 1, 2, 0], [3, 4, 5, 6], [1, 3, 1, 5]]), [[0, 0, 0, 0], [0, 4, 5, 0], [0, 3, 1, 0]]));
Check(Same(Zeroed([[0, 1], [1, 1]]), [[0, 0], [0, 1]]));
Check(Same(Zeroed([[1, 2], [3, 4]]), [[1, 2], [3, 4]]));
Check(Same(Zeroed([[0]]), [[0]]));
Check(Same(Zeroed([[5, 0, 7]]), [[0, 0, 0]]));
Console.WriteLine("020 ok");

static void ZeroOut(int[][] grid)
{
    var zeroRow = new bool[grid.Length];
    var zeroCol = new bool[grid[0].Length];
    for (var r = 0; r < grid.Length; r++)
    {
        for (var c = 0; c < grid[0].Length; c++)
        {
            if (grid[r][c] == 0) zeroRow[r] = zeroCol[c] = true;
        }
    }
    for (var r = 0; r < grid.Length; r++)
    {
        for (var c = 0; c < grid[0].Length; c++)
        {
            if (zeroRow[r] || zeroCol[c]) grid[r][c] = 0;
        }
    }
}

static int[][] Zeroed(int[][] grid)
{
    ZeroOut(grid);
    return grid;
}

static bool Same(int[][] a, int[][] b) =>
    a.Length == b.Length && a.Zip(b).All(p => p.First.SequenceEqual(p.Second));

static void Check(bool ok)
{
    if (!ok) throw new Exception("test failed");
}
