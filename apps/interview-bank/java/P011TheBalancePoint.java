// 011 · The Balance Point
import java.util.Arrays;

public class P011TheBalancePoint {
    static int balancePoint(int[] nums) {
        int total = Arrays.stream(nums).sum();
        int left = 0;
        for (int i = 0; i < nums.length; i++) {
            if (left == total - left - nums[i]) return i;
            left += nums[i];
        }
        return -1;
    }

    public static void main(String[] args) {
        check(balancePoint(new int[] {3, 1, 5, 2, 2}) == 2);
        check(balancePoint(new int[] {1, 2, 3}) == -1);
        check(balancePoint(new int[0]) == -1);
        check(balancePoint(new int[] {7}) == 0);
        check(balancePoint(new int[] {5, 2, -2}) == 0);
        check(balancePoint(new int[] {0, 0, 0}) == 0);
        System.out.println("011 ok");
    }

    static void check(boolean ok) {
        if (!ok) throw new AssertionError("test failed");
    }
}
