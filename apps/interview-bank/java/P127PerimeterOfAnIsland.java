// 127 · Perimeter of an Island
public class P127PerimeterOfAnIsland {
    static int islandPerimeter(int[][] grid) {
        int perimeter = 0;
        for (int r = 0; r < grid.length; r++) {
            for (int c = 0; c < grid[r].length; c++) {
                if (grid[r][c] != 1) continue;
                perimeter += 4;
                if (r > 0 && grid[r - 1][c] == 1) perimeter -= 2; // the shared side is lost by both cells
                if (c > 0 && grid[r][c - 1] == 1) perimeter -= 2;
            }
        }
        return perimeter;
    }

    public static void main(String[] args) {
        check(islandPerimeter(new int[][] {{0, 1, 0}, {1, 1, 1}, {0, 1, 0}}) == 12);
        check(islandPerimeter(new int[][] {{1, 1}, {1, 1}}) == 8);
        check(islandPerimeter(new int[][] {{1}}) == 4);
        check(islandPerimeter(new int[][] {{0, 0, 0}, {0, 1, 0}}) == 4);
        check(islandPerimeter(new int[][] {{1, 1, 1, 0}}) == 8);
        check(islandPerimeter(new int[][] {{1, 1, 1}, {1, 0, 1}, {1, 1, 1}}) == 16);
        System.out.println("127 ok");
    }

    static void check(boolean ok) {
        if (!ok) throw new AssertionError("test failed");
    }
}
