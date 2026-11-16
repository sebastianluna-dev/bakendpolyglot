// 169 · Region Totals on a Seat Map
var seats = new RegionSums([
    [3, 0, 1, 4],
    [5, 6, 3, 2],
    [1, 2, 0, 1],
    [4, 1, 0, 5],
]);
Check(seats.Sum(1, 1, 2, 2) == 11);
Check(seats.Sum(0, 0, 3, 3) == 38);
Check(seats.Sum(2, 3, 2, 3) == 1);
Check(seats.Sum(0, 1, 3, 2) == 13);
Check(seats.Sum(1, 0, 1, 3) == 16);
Check(seats.Sum(2, 2, 3, 3) == 6);
Check(new RegionSums([[7]]).Sum(0, 0, 0, 0) == 7);
Console.WriteLine("169 ok");

static void Check(bool ok)
{
    if (!ok) throw new Exception("test failed");
}

class RegionSums
{
    private readonly int[,] prefix; // prefix[r, c] = total of the rows above r and the columns left of c

    public RegionSums(int[][] grid)
    {
        int rows = grid.Length, cols = grid[0].Length;
        prefix = new int[rows + 1, cols + 1];
        for (var r = 0; r < rows; r++)
        {
            for (var c = 0; c < cols; c++)
            {
                prefix[r + 1, c + 1] = grid[r][c] + prefix[r, c + 1] + prefix[r + 1, c] - prefix[r, c];
            }
        }
    }

    public int Sum(int r1, int c1, int r2, int c2) =>
        prefix[r2 + 1, c2 + 1] - prefix[r1, c2 + 1] - prefix[r2 + 1, c1] + prefix[r1, c1];
}
