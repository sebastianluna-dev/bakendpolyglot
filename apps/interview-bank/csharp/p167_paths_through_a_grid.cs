// 167 · Paths Through a Grid
Check(GridPaths(3, 4) == 10);
Check(GridPaths(2, 2) == 2);
Check(GridPaths(1, 1) == 1);
Check(GridPaths(1, 5) == 1);
Check(GridPaths(5, 1) == 1);
Check(GridPaths(3, 7) == 28);
Check(GridPaths(10, 10) == 48620);
Check(GridPaths(17, 17) == 601080390);
Console.WriteLine("167 ok");

static int GridPaths(int rows, int cols)
{
    var row = new int[cols];
    Array.Fill(row, 1); // the top row: one path to each cell
    for (var r = 1; r < rows; r++)
    {
        for (var c = 1; c < cols; c++)
        {
            row[c] += row[c - 1]; // from above (old row[c]) plus from the left
        }
    }
    return row[cols - 1];
}

static void Check(bool ok)
{
    if (!ok) throw new Exception("test failed");
}
