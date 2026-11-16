// 171 · Longest Rising Sequence
public class P171LongestRisingSequence {
    static int longestRising(int[] nums) {
        int[] tails = new int[nums.length]; // tails[k] = smallest last value of a rising sequence of length k + 1
        int size = 0;
        for (int x : nums) {
            int lo = 0, hi = size;
            while (lo < hi) {
                // first tail >= x, so an equal value replaces instead of extending
                int mid = (lo + hi) / 2;
                if (tails[mid] < x) lo = mid + 1;
                else hi = mid;
            }
            tails[lo] = x;
            if (lo == size) size++;
        }
        return size;
    }

    public static void main(String[] args) {
        check(longestRising(new int[] {3, 1, 8, 2, 5, 9, 4}) == 4);
        check(longestRising(new int[] {7, 7, 7}) == 1);
        check(longestRising(new int[0]) == 0);
        check(longestRising(new int[] {5}) == 1);
        check(longestRising(new int[] {9, 8, 7}) == 1);
        check(longestRising(new int[] {1, 2, 3, 4}) == 4);
        check(longestRising(new int[] {2, 2, 3, 3, 4}) == 3);
        check(longestRising(new int[] {6, 2, 9, 3, 7, 4, 10, 5, 8}) == 5);
        check(longestRising(new int[] {10, -3, 4, -1, 6, 2, 8}) == 4);
        System.out.println("171 ok");
    }

    static void check(boolean ok) {
        if (!ok) throw new AssertionError("test failed");
    }
}
