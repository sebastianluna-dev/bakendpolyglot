// 061 · Longest Run of Ones With k Flips
public class P061LongestRunOfOnesWithKFlips {
    static int longestOnes(int[] seats, int k) {
        int left = 0, zeros = 0, best = 0; // zeros inside seats[left..right]
        for (int right = 0; right < seats.length; right++) {
            if (seats[right] == 0) zeros++;
            while (zeros > k) {
                if (seats[left] == 0) zeros--;
                left++;
            }
            best = Math.max(best, right - left + 1);
        }
        return best;
    }

    public static void main(String[] args) {
        check(longestOnes(new int[] {1, 1, 0, 1, 0, 0, 1, 1, 1, 0, 1}, 2) == 6);
        check(longestOnes(new int[] {1, 0, 1, 1, 1, 0}, 0) == 3);
        check(longestOnes(new int[0], 3) == 0);
        check(longestOnes(new int[] {0, 0, 0}, 0) == 0);
        check(longestOnes(new int[] {0, 0, 0}, 5) == 3);
        check(longestOnes(new int[] {1, 0, 0, 1}, 1) == 2);
        check(longestOnes(new int[] {1, 1, 1}, 1) == 3);
        System.out.println("061 ok");
    }

    static void check(boolean ok) {
        if (!ok) throw new AssertionError("test failed");
    }
}
