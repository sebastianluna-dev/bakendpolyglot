// 187 · Fewest Removals to Avoid Overlap
import java.util.Arrays;
import java.util.Comparator;

public class P187FewestRemovalsToAvoidOverlap {
    static int fewestRemovals(int[][] intervals) {
        int[][] byEnd = intervals.clone();
        Arrays.sort(byEnd, Comparator.comparingInt(a -> a[1]));
        int removals = 0;
        int lastEnd = Integer.MIN_VALUE; // end of the last interval kept
        for (int[] iv : byEnd) {
            if (iv[0] >= lastEnd) {
                lastEnd = iv[1];
            } else {
                removals++;
            }
        }
        return removals;
    }

    public static void main(String[] args) {
        check(fewestRemovals(new int[][] {{1, 10}, {2, 3}, {4, 5}, {6, 7}}) == 1);
        check(fewestRemovals(new int[][] {{1, 3}, {3, 5}, {5, 8}}) == 0);
        check(fewestRemovals(new int[0][]) == 0);
        check(fewestRemovals(new int[][] {{1, 2}, {1, 2}, {1, 2}}) == 2);
        check(fewestRemovals(new int[][] {{1, 4}, {2, 3}, {3, 6}, {5, 7}}) == 2);
        System.out.println("187 ok");
    }

    static void check(boolean ok) {
        if (!ok) throw new AssertionError("test failed");
    }
}
