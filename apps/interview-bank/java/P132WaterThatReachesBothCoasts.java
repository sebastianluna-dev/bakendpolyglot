// 132 · Water That Reaches Both Coasts
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Deque;
import java.util.List;

public class P132WaterThatReachesBothCoasts {
    static final int[][] DIRS = {{1, 0}, {-1, 0}, {0, 1}, {0, -1}};

    static int[][] bothCoasts(int[][] heights) {
        int rows = heights.length, cols = heights[0].length;
        List<int[]> northWestEdge = new ArrayList<>();
        List<int[]> southEastEdge = new ArrayList<>();
        for (int r = 0; r < rows; r++) {
            for (int c = 0; c < cols; c++) {
                if (r == 0 || c == 0) northWestEdge.add(new int[] {r, c});
                if (r == rows - 1 || c == cols - 1) southEastEdge.add(new int[] {r, c});
            }
        }
        boolean[][] northWest = reach(heights, northWestEdge);
        boolean[][] southEast = reach(heights, southEastEdge);
        List<int[]> cells = new ArrayList<>();
        for (int r = 0; r < rows; r++) {
            for (int c = 0; c < cols; c++) {
                if (northWest[r][c] && southEast[r][c]) cells.add(new int[] {r, c});
            }
        }
        return cells.toArray(new int[0][]);
    }

    static boolean[][] reach(int[][] heights, List<int[]> starts) {
        int rows = heights.length, cols = heights[0].length;
        boolean[][] seen = new boolean[rows][cols];
        for (int[] s : starts) seen[s[0]][s[1]] = true;
        Deque<int[]> queue = new ArrayDeque<>(starts);
        while (!queue.isEmpty()) {
            int[] cell = queue.poll();
            int r = cell[0], c = cell[1];
            for (int[] d : DIRS) {
                int nr = r + d[0], nc = c + d[1];
                // uphill or level: water from (nr, nc) could flow down to (r, c)
                if (nr < 0 || nr >= rows || nc < 0 || nc >= cols || seen[nr][nc] || heights[nr][nc] < heights[r][c]) continue;
                seen[nr][nc] = true;
                queue.offer(new int[] {nr, nc});
            }
        }
        return seen;
    }

    public static void main(String[] args) {
        check(Arrays.deepEquals(bothCoasts(new int[][] {{1, 2, 2, 3}, {3, 2, 3, 4}, {2, 4, 5, 3}, {6, 7, 1, 4}}),
                new int[][] {{0, 3}, {1, 3}, {2, 2}, {3, 0}, {3, 1}}));
        check(Arrays.deepEquals(bothCoasts(new int[][] {{5, 5, 5}, {5, 1, 5}, {5, 5, 5}}),
                new int[][] {{0, 0}, {0, 1}, {0, 2}, {1, 0}, {1, 2}, {2, 0}, {2, 1}, {2, 2}}));
        check(Arrays.deepEquals(bothCoasts(new int[][] {{4, 3, 2}, {5, 1, 3}, {6, 2, 1}}),
                new int[][] {{0, 0}, {0, 1}, {0, 2}, {1, 0}, {1, 2}, {2, 0}}));
        check(Arrays.deepEquals(bothCoasts(new int[][] {{1, 2, 3}}), new int[][] {{0, 0}, {0, 1}, {0, 2}}));
        check(Arrays.deepEquals(bothCoasts(new int[][] {{7}}), new int[][] {{0, 0}}));
        System.out.println("132 ok");
    }

    static void check(boolean ok) {
        if (!ok) throw new AssertionError("test failed");
    }
}
