// 128 · Count the Islands
Check(CountIslands([[1, 1, 0, 0, 1], [1, 0, 0, 1, 1], [0, 0, 1, 0, 0], [1, 0, 0, 0, 1]]) == 5);
Check(CountIslands([[1, 0, 1], [0, 1, 0], [1, 0, 1]]) == 5);
Check(CountIslands([[0, 0], [0, 0]]) == 0);
Check(CountIslands([[1, 1], [1, 1]]) == 1);
Check(CountIslands([[1, 1, 1], [0, 0, 1], [1, 1, 1]]) == 1);
Check(CountIslands([[1]]) == 1);
Console.WriteLine("128 ok");

static int CountIslands(int[][] grid)
{
    int rows = grid.Length, cols = grid[0].Length;
    (int Dr, int Dc)[] dirs = [(1, 0), (-1, 0), (0, 1), (0, -1)];
    var seen = new bool[rows, cols];
    var islands = 0;
    for (var sr = 0; sr < rows; sr++)
    {
        for (var sc = 0; sc < cols; sc++)
        {
            if (grid[sr][sc] != 1 || seen[sr, sc]) continue;
            islands++;
            seen[sr, sc] = true;
            var queue = new Queue<(int, int)>();
            queue.Enqueue((sr, sc));
            while (queue.Count > 0)
            {
                var (r, c) = queue.Dequeue();
                foreach (var (dr, dc) in dirs)
                {
                    int nr = r + dr, nc = c + dc;
                    if (nr < 0 || nr >= rows || nc < 0 || nc >= cols || grid[nr][nc] != 1 || seen[nr, nc]) continue;
                    seen[nr, nc] = true;
                    queue.Enqueue((nr, nc));
                }
            }
        }
    }
    return islands;
}

static void Check(bool ok)
{
    if (!ok) throw new Exception("test failed");
}
