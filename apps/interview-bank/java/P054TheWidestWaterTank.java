// 054 · The Widest Water Tank
public class P054TheWidestWaterTank {
    static int widestTank(int[] heights) {
        int left = 0, right = heights.length - 1, best = 0;
        while (left < right) {
            int area = (right - left) * Math.min(heights[left], heights[right]);
            best = Math.max(best, area);
            // The shorter wall limits every tank that keeps it, so move it inward.
            if (heights[left] <= heights[right]) left++;
            else right--;
        }
        return best;
    }

    public static void main(String[] args) {
        check(widestTank(new int[] {2, 6, 3, 1, 5, 2}) == 15);
        check(widestTank(new int[] {4, 4}) == 4);
        check(widestTank(new int[] {7}) == 0);
        check(widestTank(new int[0]) == 0);
        check(widestTank(new int[] {1, 2, 3, 4, 5}) == 6);
        check(widestTank(new int[] {0, 0, 0}) == 0);
        check(widestTank(new int[] {5, 1, 1, 1, 5}) == 20);
        System.out.println("054 ok");
    }

    static void check(boolean ok) {
        if (!ok) throw new AssertionError("test failed");
    }
}
