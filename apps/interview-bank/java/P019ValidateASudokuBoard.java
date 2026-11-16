// 019 · Validate a Sudoku Board
import java.util.Arrays;

public class P019ValidateASudokuBoard {
    static boolean isValidSudoku(String[] board) {
        boolean[][] inRow = new boolean[9][9];
        boolean[][] inCol = new boolean[9][9];
        boolean[][] inBox = new boolean[9][9];
        for (int r = 0; r < 9; r++) {
            for (int c = 0; c < 9; c++) {
                char ch = board[r].charAt(c);
                if (ch == '.') continue;
                int d = ch - '1';
                int b = r / 3 * 3 + c / 3;
                if (inRow[r][d] || inCol[c][d] || inBox[b][d]) return false;
                inRow[r][d] = inCol[c][d] = inBox[b][d] = true;
            }
        }
        return true;
    }

    static String[] change(String[] board, int r, String row) {
        String[] copy = board.clone();
        copy[r] = row;
        return copy;
    }

    public static void main(String[] args) {
        String[] board = {".9.5.3.46", "5.3.46...", "1........", "9.5.....2", "..1.6...5", "4..9....1", ".583...2.", "3.46.9758", ".2...8.1."};
        check(isValidSudoku(board));
        check(!isValidSudoku(change(board, 2, "1.9......"))); // box only
        check(!isValidSudoku(change(board, 0, "69.5.3.46"))); // row only
        check(!isValidSudoku(change(board, 4, "3.1.6...5"))); // column only
        String[] empty = new String[9];
        Arrays.fill(empty, ".........");
        check(isValidSudoku(empty));
        System.out.println("019 ok");
    }

    static void check(boolean ok) {
        if (!ok) throw new AssertionError("test failed");
    }
}
