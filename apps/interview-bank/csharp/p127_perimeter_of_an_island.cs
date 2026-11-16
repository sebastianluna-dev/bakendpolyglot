// 127 · Perimeter of an Island
Check(IslandPerimeter([[0, 1, 0], [1, 1, 1], [0, 1, 0]]) == 12);
Check(IslandPerimeter([[1, 1], [1, 1]]) == 8);
Check(IslandPerimeter([[1]]) == 4);
Check(IslandPerimeter([[0, 0, 0], [0, 1, 0]]) == 4);
Check(IslandPerimeter([[1, 1, 1, 0]]) == 8);
Check(IslandPerimeter([[1, 1, 1], [1, 0, 1], [1, 1, 1]]) == 16);
Console.WriteLine("127 ok");

static int IslandPerimeter(int[][] grid)
{
    var perimeter = 0;
    for (var r = 0; r < grid.Length; r++)
    {
        for (var c = 0; c < grid[r].Length; c++)
        {
            if (grid[r][c] != 1) continue;
            perimeter += 4;
            if (r > 0 && grid[r - 1][c] == 1) perimeter -= 2; // the shared side is lost by both cells
            if (c > 0 && grid[r][c - 1] == 1) perimeter -= 2;
        }
    }
    return perimeter;
}

static void Check(bool ok)
{
    if (!ok) throw new Exception("test failed");
}
