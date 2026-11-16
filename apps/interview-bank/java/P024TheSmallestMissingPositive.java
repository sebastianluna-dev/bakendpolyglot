// 024 · The Smallest Missing Positive

public class P024TheSmallestMissingPositive {
    static int smallestMissingPositive(int[] nums) {
        int n = nums.length;
        for (int i = 0; i < n; i++) {
            // keep swapping until slot i holds a value that has no slot or is already in its slot
            while (nums[i] >= 1 && nums[i] <= n && nums[nums[i] - 1] != nums[i]) {
                int j = nums[i] - 1;
                int tmp = nums[i];
                nums[i] = nums[j];
                nums[j] = tmp;
            }
        }
        for (int i = 0; i < n; i++) {
            if (nums[i] != i + 1) return i + 1;
        }
        return n + 1;
    }

    public static void main(String[] args) {
        check(smallestMissingPositive(new int[] {3, 1, -2, 2, 7}) == 4);
        check(smallestMissingPositive(new int[] {5, 2, 2, 9}) == 1);
        check(smallestMissingPositive(new int[0]) == 1);
        check(smallestMissingPositive(new int[] {1, 2, 3}) == 4);
        check(smallestMissingPositive(new int[] {0, -1, -5}) == 1);
        check(smallestMissingPositive(new int[] {1, 1}) == 2);
        check(smallestMissingPositive(new int[] {2, 3, 4, 1}) == 5);
        check(smallestMissingPositive(new int[] {2147483647, 1}) == 2);
        System.out.println("024 ok");
    }

    static void check(boolean ok) {
        if (!ok) throw new AssertionError("test failed");
    }
}
