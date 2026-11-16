// 135 · Shortest Clear Path in a Grid
Check(ShortestClearPath([[0, 0, 0], [1, 1, 0], [1, 1, 0]]) == 4);
Check(ShortestClearPath([[0, 1], [1, 0]]) == 2);
Check(ShortestClearPath([[0]]) == 1);
Check(ShortestClearPath([[1]]) == -1);
Check(ShortestClearPath([[1, 0], [0, 0]]) == -1);
Check(ShortestClearPath([[0, 0], [0, 1]]) == -1);
Check(ShortestClearPath([[0, 1, 0], [1, 1, 0], [0, 0, 0]]) == -1);
Check(ShortestClearPath([[0, 0, 0, 0]]) == 4);
Check(ShortestClearPath([[0, 1, 0, 0], [0, 1, 0, 1], [0, 1, 0, 1], [1, 0, 1, 0]]) == 6);
Console.WriteLine("135 ok");

static int ShortestClearPath(int[][] grid)
{
    var rows = grid.Length;
    var cols = grid[0].Length;
    if (grid[0][0] == 1 || grid[rows - 1][cols - 1] == 1) return -1;
    var cells = new int[rows, cols]; // 0 = not reached
    cells[0, 0] = 1;
    var queue = new Queue<(int R, int C)>();
    queue.Enqueue((0, 0));
    while (queue.TryDequeue(out var cell))
    {
        var (r, c) = cell;
        if (r == rows - 1 && c == cols - 1) return cells[r, c];
        for (var dr = -1; dr <= 1; dr++)
        {
            for (var dc = -1; dc <= 1; dc++)
            {
                int nr = r + dr, nc = c + dc;
                if (nr < 0 || nr >= rows || nc < 0 || nc >= cols) continue;
                if (grid[nr][nc] == 1 || cells[nr, nc] != 0) continue; // also skips (r, c) itself
                cells[nr, nc] = cells[r, c] + 1;
                queue.Enqueue((nr, nc));
            }
        }
    }
    return -1;
}

static void Check(bool ok)
{
    if (!ok) throw new Exception("test failed");
}
