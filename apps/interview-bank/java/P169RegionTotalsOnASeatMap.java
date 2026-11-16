// 169 · Region Totals on a Seat Map
public class P169RegionTotalsOnASeatMap {
    static class RegionSums {
        private final int[][] prefix; // prefix[r][c] = total of the rows above r and the columns left of c

        RegionSums(int[][] grid) {
            int rows = grid.length, cols = grid[0].length;
            prefix = new int[rows + 1][cols + 1];
            for (int r = 0; r < rows; r++) {
                for (int c = 0; c < cols; c++) {
                    prefix[r + 1][c + 1] = grid[r][c] + prefix[r][c + 1] + prefix[r + 1][c] - prefix[r][c];
                }
            }
        }

        int sum(int r1, int c1, int r2, int c2) {
            return prefix[r2 + 1][c2 + 1] - prefix[r1][c2 + 1] - prefix[r2 + 1][c1] + prefix[r1][c1];
        }
    }

    public static void main(String[] args) {
        RegionSums seats = new RegionSums(new int[][] {
            {3, 0, 1, 4},
            {5, 6, 3, 2},
            {1, 2, 0, 1},
            {4, 1, 0, 5},
        });
        check(seats.sum(1, 1, 2, 2) == 11);
        check(seats.sum(0, 0, 3, 3) == 38);
        check(seats.sum(2, 3, 2, 3) == 1);
        check(seats.sum(0, 1, 3, 2) == 13);
        check(seats.sum(1, 0, 1, 3) == 16);
        check(seats.sum(2, 2, 3, 3) == 6);
        check(new RegionSums(new int[][] {{7}}).sum(0, 0, 0, 0) == 7);
        System.out.println("169 ok");
    }

    static void check(boolean ok) {
        if (!ok) throw new AssertionError("test failed");
    }
}
