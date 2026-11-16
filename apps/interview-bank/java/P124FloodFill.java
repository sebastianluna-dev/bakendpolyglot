// 124 · Flood Fill
import java.util.ArrayDeque;
import java.util.Arrays;
import java.util.Deque;

public class P124FloodFill {
    static final int[][] DIRS = {{1, 0}, {-1, 0}, {0, 1}, {0, -1}};

    static int[][] floodFill(int[][] image, int row, int col, int color) {
        int old = image[row][col];
        if (old == color) return image; // nothing to do, and the loop below would never stop
        int rows = image.length, cols = image[0].length;
        image[row][col] = color;
        Deque<int[]> queue = new ArrayDeque<>();
        queue.offer(new int[] {row, col});
        while (!queue.isEmpty()) {
            int[] cell = queue.poll();
            for (int[] d : DIRS) {
                int nr = cell[0] + d[0], nc = cell[1] + d[1];
                if (nr < 0 || nr >= rows || nc < 0 || nc >= cols || image[nr][nc] != old) continue;
                image[nr][nc] = color;
                queue.offer(new int[] {nr, nc});
            }
        }
        return image;
    }

    public static void main(String[] args) {
        check(Arrays.deepEquals(floodFill(new int[][] {{1, 1, 0}, {0, 1, 0}, {1, 0, 1}}, 0, 0, 3),
                new int[][] {{3, 3, 0}, {0, 3, 0}, {1, 0, 1}}));
        check(Arrays.deepEquals(floodFill(new int[][] {{4, 4}, {4, 4}}, 1, 1, 4), new int[][] {{4, 4}, {4, 4}}));
        check(Arrays.deepEquals(floodFill(new int[][] {{0}}, 0, 0, 7), new int[][] {{7}}));
        check(Arrays.deepEquals(floodFill(new int[][] {{1, 2, 1}, {1, 2, 1}, {1, 1, 1}}, 0, 2, 9),
                new int[][] {{9, 2, 9}, {9, 2, 9}, {9, 9, 9}}));
        check(Arrays.deepEquals(floodFill(new int[][] {{5, 5, 6}, {6, 5, 6}}, 1, 2, 0),
                new int[][] {{5, 5, 0}, {6, 5, 0}}));
        System.out.println("124 ok");
    }

    static void check(boolean ok) {
        if (!ok) throw new AssertionError("test failed");
    }
}
