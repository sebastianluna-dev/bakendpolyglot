// 162 · Solve a Sudoku
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class P162SolveASudoku {
    static String[] solveSudoku(String[] board) {
        char[][] grid = new char[9][];
        for (int r = 0; r < 9; r++) grid[r] = board[r].toCharArray();
        boolean[][] inRow = new boolean[9][10], inCol = new boolean[9][10], inBox = new boolean[9][10];
        List<int[]> empty = new ArrayList<>();
        for (int r = 0; r < 9; r++) {
            for (int c = 0; c < 9; c++) {
                if (grid[r][c] == '.') {
                    empty.add(new int[] {r, c});
                } else {
                    int d = grid[r][c] - '0';
                    inRow[r][d] = inCol[c][d] = inBox[r / 3 * 3 + c / 3][d] = true;
                }
            }
        }
        if (!fill(0, grid, empty, inRow, inCol, inBox)) return new String[0];
        String[] solved = new String[9];
        for (int r = 0; r < 9; r++) solved[r] = new String(grid[r]);
        return solved;
    }

    static boolean fill(int k, char[][] grid, List<int[]> empty, boolean[][] inRow, boolean[][] inCol, boolean[][] inBox) {
        if (k == empty.size()) return true;
        int r = empty.get(k)[0], c = empty.get(k)[1];
        int b = r / 3 * 3 + c / 3;
        for (int d = 1; d <= 9; d++) {
            if (inRow[r][d] || inCol[c][d] || inBox[b][d]) continue;
            inRow[r][d] = inCol[c][d] = inBox[b][d] = true;
            grid[r][c] = (char) ('0' + d);
            if (fill(k + 1, grid, empty, inRow, inCol, inBox)) return true; // keep the digits: the board is solved
            inRow[r][d] = inCol[c][d] = inBox[b][d] = false;
            grid[r][c] = '.';
        }
        return false;
    }

    public static void main(String[] args) {
        String[] puzzle = {"..59..386", ".86....29", "1....3...", ".51...8..", "8.4..7...", "...46...1", ".12....4.", "..87..5..", "647.....8"};
        String[] solved = {"475921386", "386574129", "129683475", "751392864", "864157293", "293468751", "512839647", "938746512", "647215938"};
        check(Arrays.equals(solveSudoku(puzzle), solved));
        String[] stuck = new String[9];
        Arrays.fill(stuck, ".........");
        stuck[0] = "12345678.";
        stuck[1] = "........9";
        check(solveSudoku(stuck).length == 0);
        check(Arrays.equals(solveSudoku(solved), solved));
        String[] easy = {".1.52.3..", "2.57..1.6", "43...1...", "39.21..48", "......5.1", "15...7963", "76..9.48.", "5.8.7.21.", "..1...637"};
        String[] easySolved = {"619528374", "285743196", "437961852", "396215748", "874639521", "152487963", "763192485", "548376219", "921854637"};
        check(Arrays.equals(solveSudoku(easy), easySolved));
        System.out.println("162 ok");
    }

    static void check(boolean ok) {
        if (!ok) throw new AssertionError("test failed");
    }
}
