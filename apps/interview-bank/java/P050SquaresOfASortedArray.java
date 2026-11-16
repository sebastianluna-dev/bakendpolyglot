// 050 · Squares of a Sorted Array
import java.util.Arrays;

public class P050SquaresOfASortedArray {
    static int[] sortedSquares(int[] nums) {
        int[] out = new int[nums.length];
        int lo = 0, hi = nums.length - 1;
        for (int k = nums.length - 1; k >= 0; k--) {
            // the largest square left is at one of the two ends
            if (Math.abs(nums[lo]) > Math.abs(nums[hi])) {
                out[k] = nums[lo] * nums[lo];
                lo++;
            } else {
                out[k] = nums[hi] * nums[hi];
                hi--;
            }
        }
        return out;
    }

    public static void main(String[] args) {
        check(Arrays.equals(sortedSquares(new int[] {-5, -2, 0, 3, 4}), new int[] {0, 4, 9, 16, 25}));
        check(Arrays.equals(sortedSquares(new int[] {-3, -3, -1}), new int[] {1, 9, 9}));
        check(Arrays.equals(sortedSquares(new int[0]), new int[0]));
        check(Arrays.equals(sortedSquares(new int[] {2, 5}), new int[] {4, 25}));
        check(Arrays.equals(sortedSquares(new int[] {-7}), new int[] {49}));
        check(Arrays.equals(sortedSquares(new int[] {-4, -1, 1, 4}), new int[] {1, 1, 16, 16}));
        System.out.println("050 ok");
    }

    static void check(boolean ok) {
        if (!ok) throw new AssertionError("test failed");
    }
}
