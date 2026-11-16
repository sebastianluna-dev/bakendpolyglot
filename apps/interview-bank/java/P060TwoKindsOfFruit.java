// 060 · Two Kinds of Fruit
import java.util.HashMap;
import java.util.Map;

public class P060TwoKindsOfFruit {
    static int longestTwoKinds(int[] crates) {
        Map<Integer, Integer> inWindow = new HashMap<>(); // kind -> crates of it in the window
        int left = 0, best = 0;
        for (int right = 0; right < crates.length; right++) {
            inWindow.merge(crates[right], 1, Integer::sum);
            while (inWindow.size() > 2) {
                int kind = crates[left];
                int count = inWindow.get(kind) - 1;
                if (count == 0) inWindow.remove(kind); // a kind with no crates left must not count
                else inWindow.put(kind, count);
                left++;
            }
            best = Math.max(best, right - left + 1);
        }
        return best;
    }

    public static void main(String[] args) {
        check(longestTwoKinds(new int[] {3, 3, 7, 3, 9, 9, 7}) == 4);
        check(longestTwoKinds(new int[] {5, 5, 5}) == 3);
        check(longestTwoKinds(new int[0]) == 0);
        check(longestTwoKinds(new int[] {1, 2, 3, 4}) == 2);
        check(longestTwoKinds(new int[] {1, 2, 1, 3, 3, 3, 1}) == 5);
        check(longestTwoKinds(new int[] {4}) == 1);
        System.out.println("060 ok");
    }

    static void check(boolean ok) {
        if (!ok) throw new AssertionError("test failed");
    }
}
