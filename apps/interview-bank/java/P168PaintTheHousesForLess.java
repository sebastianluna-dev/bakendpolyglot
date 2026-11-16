// 168 · Paint the Houses for Less
public class P168PaintTheHousesForLess {
    static int minPaintCost(int[][] costs) {
        int red = 0, green = 0, blue = 0; // cheapest totals so far, by the colour of the last house
        for (int[] c : costs) {
            int nextRed = c[0] + Math.min(green, blue);
            int nextGreen = c[1] + Math.min(red, blue);
            int nextBlue = c[2] + Math.min(red, green);
            red = nextRed;
            green = nextGreen;
            blue = nextBlue;
        }
        return Math.min(red, Math.min(green, blue));
    }

    public static void main(String[] args) {
        check(minPaintCost(new int[][] {{8, 3, 6}, {4, 9, 2}, {7, 5, 1}}) == 8);
        check(minPaintCost(new int[][] {{1, 2, 9}, {1, 9, 9}}) == 3);
        check(minPaintCost(new int[0][]) == 0);
        check(minPaintCost(new int[][] {{5, 1, 9}}) == 1);
        check(minPaintCost(new int[][] {{7, 6, 2}, {3, 8, 9}, {4, 1, 6}, {9, 2, 3}}) == 9);
        check(minPaintCost(new int[][] {{4, 4, 4}, {4, 4, 4}}) == 8);
        System.out.println("168 ok");
    }

    static void check(boolean ok) {
        if (!ok) throw new AssertionError("test failed");
    }
}
