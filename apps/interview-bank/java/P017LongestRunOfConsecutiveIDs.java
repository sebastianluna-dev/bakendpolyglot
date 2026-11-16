// 017 · Longest Run of Consecutive IDs
import java.util.HashSet;
import java.util.Set;

public class P017LongestRunOfConsecutiveIDs {
    static int longestRun(int[] ids) {
        Set<Integer> present = new HashSet<>();
        for (int id : ids) present.add(id);
        int best = 0;
        for (int id : present) {
            if (present.contains(id - 1)) continue; // not the start of a run
            int length = 1;
            while (present.contains(id + length)) length++;
            best = Math.max(best, length);
        }
        return best;
    }

    public static void main(String[] args) {
        check(longestRun(new int[] {104, 7, 101, 5, 103, 6, 102}) == 4);
        check(longestRun(new int[] {9, 1, 9, 2, 2}) == 2);
        check(longestRun(new int[0]) == 0);
        check(longestRun(new int[] {5}) == 1);
        check(longestRun(new int[] {-1, 0, 1, 3}) == 3);
        check(longestRun(new int[] {10, 30, 20}) == 1);
        System.out.println("017 ok");
    }

    static void check(boolean ok) {
        if (!ok) throw new AssertionError("test failed");
    }
}
