// 131 · Minutes Until the Outage Spreads
import java.util.ArrayDeque;
import java.util.Deque;

public class P131MinutesUntilTheOutageSpreads {
    static final int[][] DIRS = {{1, 0}, {-1, 0}, {0, 1}, {0, -1}};

    static int minutesUntilAllFail(int[][] grid) {
        int rows = grid.length, cols = grid[0].length;
        Deque<int[]> queue = new ArrayDeque<>();
        int healthy = 0;
        for (int r = 0; r < rows; r++) {
            for (int c = 0; c < cols; c++) {
                if (grid[r][c] == 2) queue.offer(new int[] {r, c});
                else if (grid[r][c] == 1) healthy++;
            }
        }
        int minutes = 0;
        while (!queue.isEmpty() && healthy > 0) {
            minutes++;
            for (int k = queue.size(); k > 0; k--) { // one minute: only the servers that failed last minute
                int[] cell = queue.poll();
                for (int[] d : DIRS) {
                    int nr = cell[0] + d[0], nc = cell[1] + d[1];
                    if (nr < 0 || nr >= rows || nc < 0 || nc >= cols || grid[nr][nc] != 1) continue;
                    grid[nr][nc] = 2;
                    healthy--;
                    queue.offer(new int[] {nr, nc});
                }
            }
        }
        return healthy == 0 ? minutes : -1;
    }

    public static void main(String[] args) {
        check(minutesUntilAllFail(new int[][] {{1, 1, 0, 2}, {0, 1, 1, 1}, {0, 0, 0, 1}}) == 5);
        check(minutesUntilAllFail(new int[][] {{1, 1, 0, 2}, {0, 1, 1, 1}, {1, 0, 0, 1}}) == -1);
        check(minutesUntilAllFail(new int[][] {{0, 2}}) == 0);
        check(minutesUntilAllFail(new int[][] {{0}}) == 0);
        check(minutesUntilAllFail(new int[][] {{1}}) == -1);
        check(minutesUntilAllFail(new int[][] {{2, 1}}) == 1);
        check(minutesUntilAllFail(new int[][] {{2, 1, 1, 1, 2}}) == 2);
        System.out.println("131 ok");
    }

    static void check(boolean ok) {
        if (!ok) throw new AssertionError("test failed");
    }
}
