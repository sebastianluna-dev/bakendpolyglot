// 135 · Shortest Clear Path in a Grid
import java.util.ArrayDeque;
import java.util.Deque;

public class P135ShortestClearPathInAGrid {
    static int shortestClearPath(int[][] grid) {
        int rows = grid.length;
        int cols = grid[0].length;
        if (grid[0][0] == 1 || grid[rows - 1][cols - 1] == 1) return -1;
        int[][] cells = new int[rows][cols]; // 0 = not reached
        cells[0][0] = 1;
        Deque<int[]> queue = new ArrayDeque<>();
        queue.offer(new int[] {0, 0});
        while (!queue.isEmpty()) {
            int[] cell = queue.poll();
            int r = cell[0], c = cell[1];
            if (r == rows - 1 && c == cols - 1) return cells[r][c];
            for (int dr = -1; dr <= 1; dr++) {
                for (int dc = -1; dc <= 1; dc++) {
                    int nr = r + dr, nc = c + dc;
                    if (nr < 0 || nr >= rows || nc < 0 || nc >= cols) continue;
                    if (grid[nr][nc] == 1 || cells[nr][nc] != 0) continue; // also skips (r, c) itself
                    cells[nr][nc] = cells[r][c] + 1;
                    queue.offer(new int[] {nr, nc});
                }
            }
        }
        return -1;
    }

    public static void main(String[] args) {
        check(shortestClearPath(new int[][] {{0, 0, 0}, {1, 1, 0}, {1, 1, 0}}) == 4);
        check(shortestClearPath(new int[][] {{0, 1}, {1, 0}}) == 2);
        check(shortestClearPath(new int[][] {{0}}) == 1);
        check(shortestClearPath(new int[][] {{1}}) == -1);
        check(shortestClearPath(new int[][] {{1, 0}, {0, 0}}) == -1);
        check(shortestClearPath(new int[][] {{0, 0}, {0, 1}}) == -1);
        check(shortestClearPath(new int[][] {{0, 1, 0}, {1, 1, 0}, {0, 0, 0}}) == -1);
        check(shortestClearPath(new int[][] {{0, 0, 0, 0}}) == 4);
        check(shortestClearPath(new int[][] {{0, 1, 0, 0}, {0, 1, 0, 1}, {0, 1, 0, 1}, {1, 0, 1, 0}}) == 6);
        System.out.println("135 ok");
    }

    static void check(boolean ok) {
        if (!ok) throw new AssertionError("test failed");
    }
}
