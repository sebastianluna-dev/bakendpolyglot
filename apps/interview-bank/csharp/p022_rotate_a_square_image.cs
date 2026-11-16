// 022 · Rotate a Square Image
Check(SameGrid(Rotated([[1, 2], [3, 4]]), [[3, 1], [4, 2]]));
Check(SameGrid(Rotated([[11, 12, 13], [21, 22, 23], [31, 32, 33]]), [[31, 21, 11], [32, 22, 12], [33, 23, 13]]));
Check(SameGrid(Rotated([]), []));
Check(SameGrid(Rotated([[7]]), [[7]]));
Check(SameGrid(
    Rotated([[1, 2, 3, 4], [5, 6, 7, 8], [9, 10, 11, 12], [13, 14, 15, 16]]),
    [[13, 9, 5, 1], [14, 10, 6, 2], [15, 11, 7, 3], [16, 12, 8, 4]]));
Console.WriteLine("022 ok");

static void RotateImage(int[][] image)
{
    var n = image.Length;
    for (var i = 0; i < n; i++)
        for (var j = i + 1; j < n; j++)
            (image[i][j], image[j][i]) = (image[j][i], image[i][j]);
    foreach (var row in image) Array.Reverse(row);
}

static int[][] Rotated(int[][] image)
{
    RotateImage(image);
    return image;
}

static bool SameGrid(int[][] a, int[][] b) =>
    a.Length == b.Length && a.Zip(b).All(p => p.First.SequenceEqual(p.Second));

static void Check(bool ok)
{
    if (!ok) throw new Exception("test failed");
}
