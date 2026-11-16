// 131 · Minutes Until the Outage Spreads
Check(MinutesUntilAllFail([[1, 1, 0, 2], [0, 1, 1, 1], [0, 0, 0, 1]]) == 5);
Check(MinutesUntilAllFail([[1, 1, 0, 2], [0, 1, 1, 1], [1, 0, 0, 1]]) == -1);
Check(MinutesUntilAllFail([[0, 2]]) == 0);
Check(MinutesUntilAllFail([[0]]) == 0);
Check(MinutesUntilAllFail([[1]]) == -1);
Check(MinutesUntilAllFail([[2, 1]]) == 1);
Check(MinutesUntilAllFail([[2, 1, 1, 1, 2]]) == 2);
Console.WriteLine("131 ok");

static int MinutesUntilAllFail(int[][] grid)
{
    int rows = grid.Length, cols = grid[0].Length;
    (int Dr, int Dc)[] dirs = [(1, 0), (-1, 0), (0, 1), (0, -1)];
    var queue = new Queue<(int, int)>();
    var healthy = 0;
    for (var r = 0; r < rows; r++)
    {
        for (var c = 0; c < cols; c++)
        {
            if (grid[r][c] == 2) queue.Enqueue((r, c));
            else if (grid[r][c] == 1) healthy++;
        }
    }
    var minutes = 0;
    while (queue.Count > 0 && healthy > 0)
    {
        minutes++;
        for (var k = queue.Count; k > 0; k--) // one minute: only the servers that failed last minute
        {
            var (r, c) = queue.Dequeue();
            foreach (var (dr, dc) in dirs)
            {
                int nr = r + dr, nc = c + dc;
                if (nr < 0 || nr >= rows || nc < 0 || nc >= cols || grid[nr][nc] != 1) continue;
                grid[nr][nc] = 2;
                healthy--;
                queue.Enqueue((nr, nc));
            }
        }
    }
    return healthy == 0 ? minutes : -1;
}

static void Check(bool ok)
{
    if (!ok) throw new Exception("test failed");
}
