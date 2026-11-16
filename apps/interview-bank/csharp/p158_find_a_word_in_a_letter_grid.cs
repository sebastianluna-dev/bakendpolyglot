// 158 · Find a Word in a Letter Grid
string[] grid = ["she", "afl", "nao"];
Check(FindWord(grid, "shelf"));
Check(!FindWord(grid, "lol"));
Check(FindWord(grid, "loan"));
Check(FindWord(grid, "fan"));
Check(!FindWord(grid, "sf"));
Check(FindWord(grid, "o"));
Check(!FindWord(grid, "z"));
Check(!FindWord(grid, "shelfaloans"));
Check(FindWord(["aa", "ab"], "aaab"));
Console.WriteLine("158 ok");

static bool FindWord(string[] grid, string word)
{
    int rows = grid.Length, cols = grid[0].Length;
    var used = new bool[rows, cols];
    bool Walk(int r, int c, int k)
    {
        if (k == word.Length) return true;
        if (r < 0 || r >= rows || c < 0 || c >= cols || used[r, c] || grid[r][c] != word[k]) return false;
        used[r, c] = true;
        var found = Walk(r + 1, c, k + 1) || Walk(r - 1, c, k + 1) || Walk(r, c + 1, k + 1) || Walk(r, c - 1, k + 1);
        used[r, c] = false; // free the cell for paths that start elsewhere
        return found;
    }
    for (var r = 0; r < rows; r++)
    {
        for (var c = 0; c < cols; c++)
        {
            if (Walk(r, c, 0)) return true;
        }
    }
    return false;
}

static void Check(bool ok)
{
    if (!ok) throw new Exception("test failed");
}
