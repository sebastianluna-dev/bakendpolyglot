// 016 · Product of Everything Else
import java.util.Arrays;

public class P016ProductOfEverythingElse {
    static int[] productOfOthers(int[] nums) {
        int[] out = new int[nums.length];
        int left = 1;
        for (int i = 0; i < nums.length; i++) {
            out[i] = left; // product of everything before i
            left *= nums[i];
        }
        int right = 1;
        for (int i = nums.length - 1; i >= 0; i--) {
            out[i] *= right;
            right *= nums[i];
        }
        return out;
    }

    public static void main(String[] args) {
        check(Arrays.equals(productOfOthers(new int[] {2, 3, 4, 5}), new int[] {60, 40, 30, 24}));
        check(Arrays.equals(productOfOthers(new int[] {4, 0, 5}), new int[] {0, 20, 0}));
        check(Arrays.equals(productOfOthers(new int[] {3, 0, 2, 0}), new int[] {0, 0, 0, 0}));
        check(Arrays.equals(productOfOthers(new int[] {-1, 2, -3}), new int[] {-6, 3, -2}));
        check(Arrays.equals(productOfOthers(new int[] {7}), new int[] {1}));
        check(Arrays.equals(productOfOthers(new int[0]), new int[0]));
        System.out.println("016 ok");
    }

    static void check(boolean ok) {
        if (!ok) throw new AssertionError("test failed");
    }
}
