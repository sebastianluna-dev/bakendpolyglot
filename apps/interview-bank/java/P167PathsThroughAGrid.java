// 167 · Paths Through a Grid
import java.util.Arrays;

public class P167PathsThroughAGrid {
    static int gridPaths(int rows, int cols) {
        int[] row = new int[cols];
        Arrays.fill(row, 1); // the top row: one path to each cell
        for (int r = 1; r < rows; r++) {
            for (int c = 1; c < cols; c++) {
                row[c] += row[c - 1]; // from above (old row[c]) plus from the left
            }
        }
        return row[cols - 1];
    }

    public static void main(String[] args) {
        check(gridPaths(3, 4) == 10);
        check(gridPaths(2, 2) == 2);
        check(gridPaths(1, 1) == 1);
        check(gridPaths(1, 5) == 1);
        check(gridPaths(5, 1) == 1);
        check(gridPaths(3, 7) == 28);
        check(gridPaths(10, 10) == 48620);
        check(gridPaths(17, 17) == 601080390);
        System.out.println("167 ok");
    }

    static void check(boolean ok) {
        if (!ok) throw new AssertionError("test failed");
    }
}
