// 162 · Solve a Sudoku
string[] puzzle = ["..59..386", ".86....29", "1....3...", ".51...8..", "8.4..7...", "...46...1", ".12....4.", "..87..5..", "647.....8"];
string[] solved = ["475921386", "386574129", "129683475", "751392864", "864157293", "293468751", "512839647", "938746512", "647215938"];
Check(SolveSudoku(puzzle).SequenceEqual(solved));
string[] stuck = ["12345678.", "........9", .. Enumerable.Repeat(".........", 7)];
Check(SolveSudoku(stuck).Length == 0);
Check(SolveSudoku(solved).SequenceEqual(solved));
string[] easy = [".1.52.3..", "2.57..1.6", "43...1...", "39.21..48", "......5.1", "15...7963", "76..9.48.", "5.8.7.21.", "..1...637"];
string[] easySolved = ["619528374", "285743196", "437961852", "396215748", "874639521", "152487963", "763192485", "548376219", "921854637"];
Check(SolveSudoku(easy).SequenceEqual(easySolved));
Console.WriteLine("162 ok");

static string[] SolveSudoku(string[] board)
{
    var grid = board.Select(row => row.ToCharArray()).ToArray();
    var inRow = new bool[9, 10];
    var inCol = new bool[9, 10];
    var inBox = new bool[9, 10];
    var empty = new List<(int R, int C)>();
    for (var r = 0; r < 9; r++)
    {
        for (var c = 0; c < 9; c++)
        {
            if (grid[r][c] == '.')
            {
                empty.Add((r, c));
            }
            else
            {
                var d = grid[r][c] - '0';
                inRow[r, d] = inCol[c, d] = inBox[r / 3 * 3 + c / 3, d] = true;
            }
        }
    }
    bool Fill(int k)
    {
        if (k == empty.Count) return true;
        var (r, c) = empty[k];
        var b = r / 3 * 3 + c / 3;
        for (var d = 1; d <= 9; d++)
        {
            if (inRow[r, d] || inCol[c, d] || inBox[b, d]) continue;
            inRow[r, d] = inCol[c, d] = inBox[b, d] = true;
            grid[r][c] = (char)('0' + d);
            if (Fill(k + 1)) return true; // keep the digits: the board is solved
            inRow[r, d] = inCol[c, d] = inBox[b, d] = false;
            grid[r][c] = '.';
        }
        return false;
    }
    return Fill(0) ? grid.Select(row => new string(row)).ToArray() : [];
}

static void Check(bool ok)
{
    if (!ok) throw new Exception("test failed");
}
