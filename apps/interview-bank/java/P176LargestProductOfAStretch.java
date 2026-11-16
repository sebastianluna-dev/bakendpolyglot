// 176 · Largest Product of a Stretch
import java.util.Arrays;

public class P176LargestProductOfAStretch {
    static int largestProduct(int[] nums) {
        int best = nums[0];
        int hi = nums[0]; // largest product of a stretch that ends at the current element
        int lo = nums[0]; // smallest one: a negative number turns it into the largest
        for (int i = 1; i < nums.length; i++) {
            int x = nums[i];
            int a = hi * x, b = lo * x;
            hi = Math.max(x, Math.max(a, b));
            lo = Math.min(x, Math.min(a, b));
            best = Math.max(best, hi);
        }
        return best;
    }

    public static void main(String[] args) {
        int[] tens = new int[9];
        Arrays.fill(tens, 10);
        check(largestProduct(new int[] {2, -5, -2, 4, 0, 3}) == 80);
        check(largestProduct(new int[] {-3, 0, -2}) == 0);
        check(largestProduct(new int[] {-2}) == -2);
        check(largestProduct(new int[] {-2, 3, -4}) == 24);
        check(largestProduct(new int[] {0, 0}) == 0);
        check(largestProduct(new int[] {-1, -1, -1}) == 1);
        check(largestProduct(new int[] {3, -1, 4}) == 4);
        check(largestProduct(new int[] {-4, 0, -5, 0, -6, 2}) == 2);
        check(largestProduct(tens) == 1000000000);
        System.out.println("176 ok");
    }

    static void check(boolean ok) {
        if (!ok) throw new AssertionError("test failed");
    }
}
