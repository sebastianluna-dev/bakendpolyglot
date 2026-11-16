// 128 · Count the Islands
import java.util.ArrayDeque;
import java.util.Deque;

public class P128CountTheIslands {
    static final int[][] DIRS = {{1, 0}, {-1, 0}, {0, 1}, {0, -1}};

    static int countIslands(int[][] grid) {
        int rows = grid.length, cols = grid[0].length;
        boolean[][] seen = new boolean[rows][cols];
        int islands = 0;
        for (int sr = 0; sr < rows; sr++) {
            for (int sc = 0; sc < cols; sc++) {
                if (grid[sr][sc] != 1 || seen[sr][sc]) continue;
                islands++;
                seen[sr][sc] = true;
                Deque<int[]> queue = new ArrayDeque<>();
                queue.offer(new int[] {sr, sc});
                while (!queue.isEmpty()) {
                    int[] cell = queue.poll();
                    for (int[] d : DIRS) {
                        int nr = cell[0] + d[0], nc = cell[1] + d[1];
                        if (nr < 0 || nr >= rows || nc < 0 || nc >= cols || grid[nr][nc] != 1 || seen[nr][nc]) continue;
                        seen[nr][nc] = true;
                        queue.offer(new int[] {nr, nc});
                    }
                }
            }
        }
        return islands;
    }

    public static void main(String[] args) {
        check(countIslands(new int[][] {{1, 1, 0, 0, 1}, {1, 0, 0, 1, 1}, {0, 0, 1, 0, 0}, {1, 0, 0, 0, 1}}) == 5);
        check(countIslands(new int[][] {{1, 0, 1}, {0, 1, 0}, {1, 0, 1}}) == 5);
        check(countIslands(new int[][] {{0, 0}, {0, 0}}) == 0);
        check(countIslands(new int[][] {{1, 1}, {1, 1}}) == 1);
        check(countIslands(new int[][] {{1, 1, 1}, {0, 0, 1}, {1, 1, 1}}) == 1);
        check(countIslands(new int[][] {{1}}) == 1);
        System.out.println("128 ok");
    }

    static void check(boolean ok) {
        if (!ok) throw new AssertionError("test failed");
    }
}
