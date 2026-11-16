// 010 · Number Triangle Rows
Check(SameRows(TriangleRows(5), [[1], [1, 1], [1, 2, 1], [1, 3, 3, 1], [1, 4, 6, 4, 1]]));
Check(SameRows(TriangleRows(2), [[1], [1, 1]]));
Check(SameRows(TriangleRows(1), [[1]]));
Check(TriangleRows(0).Length == 0);
Check(TriangleRows(30)[29][14] == 77558760);
Console.WriteLine("010 ok");

static int[][] TriangleRows(int n)
{
    var rows = new int[n][];
    for (var r = 0; r < n; r++)
    {
        var row = Enumerable.Repeat(1, r + 1).ToArray();
        for (var c = 1; c < r; c++) row[c] = rows[r - 1][c - 1] + rows[r - 1][c];
        rows[r] = row;
    }
    return rows;
}

static bool SameRows(int[][] a, int[][] b) =>
    a.Length == b.Length && a.Zip(b).All(p => p.First.SequenceEqual(p.Second));

static void Check(bool ok)
{
    if (!ok) throw new Exception("test failed");
}
