// 025 · Most Points on One Line
import java.util.HashMap;
import java.util.Map;

public class P025MostPointsOnOneLine {
    record Direction(int dx, int dy) {}

    static int maxPointsOnLine(int[][] points) {
        int best = 0;
        for (int i = 0; i < points.length; i++) {
            Map<Direction, Integer> directions = new HashMap<>(); // reduced direction -> points after i on it
            int same = 1; // point i and its copies
            int most = 0;
            for (int j = i + 1; j < points.length; j++) {
                int dx = points[j][0] - points[i][0];
                int dy = points[j][1] - points[i][1];
                if (dx == 0 && dy == 0) {
                    same++;
                    continue;
                }
                int g = gcd(Math.abs(dx), Math.abs(dy));
                dx /= g;
                dy /= g;
                // (1, -1) and (-1, 1) are the same line: make dx positive, or dy if dx is 0
                if (dx < 0 || (dx == 0 && dy < 0)) {
                    dx = -dx;
                    dy = -dy;
                }
                int count = directions.merge(new Direction(dx, dy), 1, Integer::sum);
                most = Math.max(most, count);
            }
            best = Math.max(best, same + most);
        }
        return best;
    }

    static int gcd(int a, int b) {
        while (b != 0) {
            int t = a % b;
            a = b;
            b = t;
        }
        return a;
    }

    public static void main(String[] args) {
        check(maxPointsOnLine(new int[][] {{1, 1}, {2, 3}, {3, 5}, {4, 2}, {0, -1}}) == 4);
        check(maxPointsOnLine(new int[][] {{3, 4}, {3, 4}, {3, -1}, {0, 0}}) == 3);
        check(maxPointsOnLine(new int[0][]) == 0);
        check(maxPointsOnLine(new int[][] {{5, 5}}) == 1);
        check(maxPointsOnLine(new int[][] {{1, 1}, {1, 1}, {1, 1}}) == 3);
        check(maxPointsOnLine(new int[][] {{0, 0}, {1, 1}, {2, 0}}) == 2);
        check(maxPointsOnLine(new int[][] {{-2, 7}, {0, 7}, {9, 7}, {1, 1}}) == 3);
        check(maxPointsOnLine(new int[][] {{0, 0}, {1, -1}, {-1, 1}, {2, -2}}) == 4);
        System.out.println("025 ok");
    }

    static void check(boolean ok) {
        if (!ok) throw new AssertionError("test failed");
    }
}
