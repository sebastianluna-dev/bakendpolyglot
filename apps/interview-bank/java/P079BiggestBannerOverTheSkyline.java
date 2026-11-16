// 079 · Biggest Banner Over the Skyline
import java.util.ArrayDeque;
import java.util.Deque;

public class P079BiggestBannerOverTheSkyline {
    static int biggestBanner(int[] heights) {
        int best = 0;
        Deque<Integer> stack = new ArrayDeque<>(); // indices of rising heights
        for (int i = 0; i <= heights.length; i++) {
            int h = i < heights.length ? heights[i] : 0; // a zero at the end empties the stack
            while (!stack.isEmpty() && heights[stack.peek()] >= h) {
                int top = stack.pop();
                int left = stack.isEmpty() ? -1 : stack.peek();
                best = Math.max(best, heights[top] * (i - left - 1));
            }
            stack.push(i);
        }
        return best;
    }

    public static void main(String[] args) {
        check(biggestBanner(new int[] {3, 1, 4, 5, 4, 2}) == 12);
        check(biggestBanner(new int[] {2, 4, 2}) == 6);
        check(biggestBanner(new int[0]) == 0);
        check(biggestBanner(new int[] {7}) == 7);
        check(biggestBanner(new int[] {1, 2, 3, 4, 5}) == 9);
        check(biggestBanner(new int[] {5, 4, 3, 2, 1}) == 9);
        check(biggestBanner(new int[] {2, 1, 2}) == 3);
        check(biggestBanner(new int[] {0, 0}) == 0);
        check(biggestBanner(new int[] {4, 0, 4}) == 4);
        System.out.println("079 ok");
    }

    static void check(boolean ok) {
        if (!ok) throw new AssertionError("test failed");
    }
}
