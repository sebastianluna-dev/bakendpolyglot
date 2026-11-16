// 025 · Most Points on One Line
Check(MaxPointsOnLine([[1, 1], [2, 3], [3, 5], [4, 2], [0, -1]]) == 4);
Check(MaxPointsOnLine([[3, 4], [3, 4], [3, -1], [0, 0]]) == 3);
Check(MaxPointsOnLine([]) == 0);
Check(MaxPointsOnLine([[5, 5]]) == 1);
Check(MaxPointsOnLine([[1, 1], [1, 1], [1, 1]]) == 3);
Check(MaxPointsOnLine([[0, 0], [1, 1], [2, 0]]) == 2);
Check(MaxPointsOnLine([[-2, 7], [0, 7], [9, 7], [1, 1]]) == 3);
Check(MaxPointsOnLine([[0, 0], [1, -1], [-1, 1], [2, -2]]) == 4);
Console.WriteLine("025 ok");

static int MaxPointsOnLine(int[][] points)
{
    var best = 0;
    for (var i = 0; i < points.Length; i++)
    {
        var directions = new Dictionary<(int, int), int>(); // reduced direction -> points after i on it
        var same = 1; // point i and its copies
        var most = 0;
        for (var j = i + 1; j < points.Length; j++)
        {
            var dx = points[j][0] - points[i][0];
            var dy = points[j][1] - points[i][1];
            if (dx == 0 && dy == 0)
            {
                same++;
                continue;
            }
            var g = Gcd(Math.Abs(dx), Math.Abs(dy));
            dx /= g;
            dy /= g;
            // (1, -1) and (-1, 1) are the same line: make dx positive, or dy if dx is 0
            if (dx < 0 || (dx == 0 && dy < 0))
            {
                dx = -dx;
                dy = -dy;
            }
            var count = directions.GetValueOrDefault((dx, dy)) + 1;
            directions[(dx, dy)] = count;
            most = Math.Max(most, count);
        }
        best = Math.Max(best, same + most);
    }
    return best;
}

static int Gcd(int a, int b)
{
    while (b != 0) (a, b) = (b, a % b);
    return a;
}

static void Check(bool ok)
{
    if (!ok) throw new Exception("test failed");
}
