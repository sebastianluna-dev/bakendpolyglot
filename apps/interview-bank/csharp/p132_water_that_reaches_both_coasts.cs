// 132 · Water That Reaches Both Coasts
Check(BothCoasts([[1, 2, 2, 3], [3, 2, 3, 4], [2, 4, 5, 3], [6, 7, 1, 4]]).SequenceEqual([(0, 3), (1, 3), (2, 2), (3, 0), (3, 1)]));
Check(BothCoasts([[5, 5, 5], [5, 1, 5], [5, 5, 5]]).SequenceEqual([(0, 0), (0, 1), (0, 2), (1, 0), (1, 2), (2, 0), (2, 1), (2, 2)]));
Check(BothCoasts([[4, 3, 2], [5, 1, 3], [6, 2, 1]]).SequenceEqual([(0, 0), (0, 1), (0, 2), (1, 0), (1, 2), (2, 0)]));
Check(BothCoasts([[1, 2, 3]]).SequenceEqual([(0, 0), (0, 1), (0, 2)]));
Check(BothCoasts([[7]]).SequenceEqual([(0, 0)]));
Console.WriteLine("132 ok");

static List<(int Row, int Col)> BothCoasts(int[][] heights)
{
    int rows = heights.Length, cols = heights[0].Length;
    var northWestEdge = new List<(int, int)>();
    var southEastEdge = new List<(int, int)>();
    for (var r = 0; r < rows; r++)
    {
        for (var c = 0; c < cols; c++)
        {
            if (r == 0 || c == 0) northWestEdge.Add((r, c));
            if (r == rows - 1 || c == cols - 1) southEastEdge.Add((r, c));
        }
    }
    var northWest = Reach(heights, northWestEdge);
    var southEast = Reach(heights, southEastEdge);
    var cells = new List<(int, int)>();
    for (var r = 0; r < rows; r++)
    {
        for (var c = 0; c < cols; c++)
        {
            if (northWest[r, c] && southEast[r, c]) cells.Add((r, c));
        }
    }
    return cells;
}

static bool[,] Reach(int[][] heights, List<(int, int)> starts)
{
    int rows = heights.Length, cols = heights[0].Length;
    (int Dr, int Dc)[] dirs = [(1, 0), (-1, 0), (0, 1), (0, -1)];
    var seen = new bool[rows, cols];
    foreach (var (r, c) in starts) seen[r, c] = true;
    var queue = new Queue<(int, int)>(starts);
    while (queue.Count > 0)
    {
        var (r, c) = queue.Dequeue();
        foreach (var (dr, dc) in dirs)
        {
            int nr = r + dr, nc = c + dc;
            // uphill or level: water from (nr, nc) could flow down to (r, c)
            if (nr < 0 || nr >= rows || nc < 0 || nc >= cols || seen[nr, nc] || heights[nr][nc] < heights[r][c]) continue;
            seen[nr, nc] = true;
            queue.Enqueue((nr, nc));
        }
    }
    return seen;
}

static void Check(bool ok)
{
    if (!ok) throw new Exception("test failed");
}
