// 021 · Read a Matrix in a Spiral
import java.util.ArrayList;
import java.util.List;

public class P021ReadAMatrixInASpiral {
    static List<Integer> spiralOrder(int[][] grid) {
        List<Integer> result = new ArrayList<>();
        if (grid.length == 0) return result;
        int top = 0, bottom = grid.length - 1, left = 0, right = grid[0].length - 1;
        while (top <= bottom && left <= right) {
            for (int c = left; c <= right; c++) result.add(grid[top][c]);
            top++;
            for (int r = top; r <= bottom; r++) result.add(grid[r][right]);
            right--;
            // after the two steps above, a single row or column may be used up
            if (top <= bottom) {
                for (int c = right; c >= left; c--) result.add(grid[bottom][c]);
                bottom--;
            }
            if (left <= right) {
                for (int r = bottom; r >= top; r--) result.add(grid[r][left]);
                left++;
            }
        }
        return result;
    }

    public static void main(String[] args) {
        check(spiralOrder(new int[][] {{1, 2, 3}, {8, 9, 4}, {7, 6, 5}}).equals(List.of(1, 2, 3, 4, 5, 6, 7, 8, 9)));
        check(spiralOrder(new int[][] {{10, 11, 12, 13}, {20, 21, 22, 23}, {30, 31, 32, 33}})
                .equals(List.of(10, 11, 12, 13, 23, 33, 32, 31, 30, 20, 21, 22)));
        check(spiralOrder(new int[0][]).isEmpty());
        check(spiralOrder(new int[][] {{4, 5, 6}}).equals(List.of(4, 5, 6)));
        check(spiralOrder(new int[][] {{4}, {5}, {6}}).equals(List.of(4, 5, 6)));
        check(spiralOrder(new int[][] {{1, 2}, {3, 4}, {5, 6}}).equals(List.of(1, 2, 4, 6, 5, 3)));
        System.out.println("021 ok");
    }

    static void check(boolean ok) {
        if (!ok) throw new AssertionError("test failed");
    }
}
