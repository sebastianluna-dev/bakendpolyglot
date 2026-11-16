// 020 · Zero Out Rows and Columns
import java.util.Arrays;

public class P020ZeroOutRowsAndColumns {
    static void zeroOut(int[][] grid) {
        boolean[] zeroRow = new boolean[grid.length];
        boolean[] zeroCol = new boolean[grid[0].length];
        for (int r = 0; r < grid.length; r++) {
            for (int c = 0; c < grid[0].length; c++) {
                if (grid[r][c] == 0) zeroRow[r] = zeroCol[c] = true;
            }
        }
        for (int r = 0; r < grid.length; r++) {
            for (int c = 0; c < grid[0].length; c++) {
                if (zeroRow[r] || zeroCol[c]) grid[r][c] = 0;
            }
        }
    }

    static int[][] zeroed(int[][] grid) {
        zeroOut(grid);
        return grid;
    }

    public static void main(String[] args) {
        check(Arrays.deepEquals(zeroed(new int[][] {{1, 2, 3}, {4, 0, 6}, {7, 8, 9}}), new int[][] {{1, 0, 3}, {0, 0, 0}, {7, 0, 9}}));
        check(Arrays.deepEquals(zeroed(new int[][] {{0, 1, 2, 0}, {3, 4, 5, 6}, {1, 3, 1, 5}}), new int[][] {{0, 0, 0, 0}, {0, 4, 5, 0}, {0, 3, 1, 0}}));
        check(Arrays.deepEquals(zeroed(new int[][] {{0, 1}, {1, 1}}), new int[][] {{0, 0}, {0, 1}}));
        check(Arrays.deepEquals(zeroed(new int[][] {{1, 2}, {3, 4}}), new int[][] {{1, 2}, {3, 4}}));
        check(Arrays.deepEquals(zeroed(new int[][] {{0}}), new int[][] {{0}}));
        check(Arrays.deepEquals(zeroed(new int[][] {{5, 0, 7}}), new int[][] {{0, 0, 0}}));
        System.out.println("020 ok");
    }

    static void check(boolean ok) {
        if (!ok) throw new AssertionError("test failed");
    }
}
