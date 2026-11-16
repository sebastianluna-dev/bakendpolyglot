// 161 · Place n Queens
Check(Same(PlaceQueens(4), [[".Q..", "...Q", "Q...", "..Q."], ["..Q.", "Q...", "...Q", ".Q.."]]));
Check(Same(PlaceQueens(1), [["Q"]]));
Check(PlaceQueens(2).Count == 0);
Check(PlaceQueens(3).Count == 0);
Check(PlaceQueens(6).Count == 4);
var eight = PlaceQueens(8);
Check(eight.Count == 92);
Check(eight[0].SequenceEqual(["Q.......", "....Q...", ".......Q", ".....Q..", "..Q.....", "......Q.", ".Q......", "...Q...."]));
Console.WriteLine("161 ok");

static List<string[]> PlaceQueens(int n)
{
    var result = new List<string[]>();
    var queens = new List<int>(); // queens[r] is the column of the queen in row r
    var cols = new bool[n];
    var downs = new bool[2 * n - 1]; // r + c is the same along a down-left diagonal
    var ups = new bool[2 * n - 1]; // r - c + n - 1 is the same along a down-right diagonal
    void Walk(int r)
    {
        if (r == n)
        {
            result.Add(queens.Select(c => new string('.', c) + "Q" + new string('.', n - c - 1)).ToArray());
            return;
        }
        for (var c = 0; c < n; c++)
        {
            if (cols[c] || downs[r + c] || ups[r - c + n - 1]) continue;
            cols[c] = downs[r + c] = ups[r - c + n - 1] = true;
            queens.Add(c);
            Walk(r + 1);
            queens.RemoveAt(queens.Count - 1);
            cols[c] = downs[r + c] = ups[r - c + n - 1] = false;
        }
    }
    Walk(0);
    return result;
}

static bool Same(List<string[]> got, string[][] want) =>
    got.Count == want.Length && got.Zip(want).All(p => p.First.SequenceEqual(p.Second));

static void Check(bool ok)
{
    if (!ok) throw new Exception("test failed");
}
