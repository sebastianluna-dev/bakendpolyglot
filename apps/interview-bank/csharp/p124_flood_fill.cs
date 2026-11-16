// 124 · Flood Fill
Check(Same(FloodFill([[1, 1, 0], [0, 1, 0], [1, 0, 1]], 0, 0, 3), [[3, 3, 0], [0, 3, 0], [1, 0, 1]]));
Check(Same(FloodFill([[4, 4], [4, 4]], 1, 1, 4), [[4, 4], [4, 4]]));
Check(Same(FloodFill([[0]], 0, 0, 7), [[7]]));
Check(Same(FloodFill([[1, 2, 1], [1, 2, 1], [1, 1, 1]], 0, 2, 9), [[9, 2, 9], [9, 2, 9], [9, 9, 9]]));
Check(Same(FloodFill([[5, 5, 6], [6, 5, 6]], 1, 2, 0), [[5, 5, 0], [6, 5, 0]]));
Console.WriteLine("124 ok");

static int[][] FloodFill(int[][] image, int row, int col, int color)
{
    var old = image[row][col];
    if (old == color) return image; // nothing to do, and the loop below would never stop
    int rows = image.Length, cols = image[0].Length;
    (int Dr, int Dc)[] dirs = [(1, 0), (-1, 0), (0, 1), (0, -1)];
    image[row][col] = color;
    var queue = new Queue<(int, int)>();
    queue.Enqueue((row, col));
    while (queue.Count > 0)
    {
        var (r, c) = queue.Dequeue();
        foreach (var (dr, dc) in dirs)
        {
            int nr = r + dr, nc = c + dc;
            if (nr < 0 || nr >= rows || nc < 0 || nc >= cols || image[nr][nc] != old) continue;
            image[nr][nc] = color;
            queue.Enqueue((nr, nc));
        }
    }
    return image;
}

static bool Same(int[][] a, int[][] b) => a.Length == b.Length && a.Zip(b).All(p => p.First.SequenceEqual(p.Second));

static void Check(bool ok)
{
    if (!ok) throw new Exception("test failed");
}
