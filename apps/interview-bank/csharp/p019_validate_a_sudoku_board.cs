// 019 · Validate a Sudoku Board
string[] board = [".9.5.3.46", "5.3.46...", "1........", "9.5.....2", "..1.6...5", "4..9....1", ".583...2.", "3.46.9758", ".2...8.1."];
Check(IsValidSudoku(board));
Check(!IsValidSudoku(Change(board, 2, "1.9......"))); // box only
Check(!IsValidSudoku(Change(board, 0, "69.5.3.46"))); // row only
Check(!IsValidSudoku(Change(board, 4, "3.1.6...5"))); // column only
Check(IsValidSudoku(Enumerable.Repeat(".........", 9).ToArray()));
Console.WriteLine("019 ok");

static bool IsValidSudoku(string[] board)
{
    var inRow = new bool[9, 9];
    var inCol = new bool[9, 9];
    var inBox = new bool[9, 9];
    for (var r = 0; r < 9; r++)
    {
        for (var c = 0; c < 9; c++)
        {
            var ch = board[r][c];
            if (ch == '.') continue;
            var d = ch - '1';
            var b = r / 3 * 3 + c / 3;
            if (inRow[r, d] || inCol[c, d] || inBox[b, d]) return false;
            inRow[r, d] = inCol[c, d] = inBox[b, d] = true;
        }
    }
    return true;
}

static string[] Change(string[] board, int r, string row)
{
    var copy = (string[])board.Clone();
    copy[r] = row;
    return copy;
}

static void Check(bool ok)
{
    if (!ok) throw new Exception("test failed");
}
