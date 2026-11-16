// 018 · Count Stretches With a Target Sum
import java.util.HashMap;
import java.util.Map;

public class P018CountStretchesWithATargetSum {
    static int countStretches(int[] changes, int target) {
        Map<Integer, Integer> seen = new HashMap<>(); // prefix sum -> how many times
        seen.put(0, 1); // the empty prefix
        int sum = 0;
        int count = 0;
        for (int change : changes) {
            sum += change;
            count += seen.getOrDefault(sum - target, 0);
            seen.merge(sum, 1, Integer::sum);
        }
        return count;
    }

    public static void main(String[] args) {
        check(countStretches(new int[] {1, 2, 3, -2, 2}, 3) == 4);
        check(countStretches(new int[] {0, 0, 0}, 0) == 6);
        check(countStretches(new int[0], 0) == 0);
        check(countStretches(new int[] {5}, 3) == 0);
        check(countStretches(new int[] {3}, 3) == 1);
        check(countStretches(new int[] {1, -1, 1, -1}, 0) == 4);
        System.out.println("018 ok");
    }

    static void check(boolean ok) {
        if (!ok) throw new AssertionError("test failed");
    }
}
