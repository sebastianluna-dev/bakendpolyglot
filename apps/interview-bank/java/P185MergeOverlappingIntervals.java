// 185 · Merge Overlapping Intervals
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;

public class P185MergeOverlappingIntervals {
    static int[][] mergeIntervals(int[][] intervals) {
        int[][] sorted = intervals.clone();
        Arrays.sort(sorted, Comparator.comparingInt(a -> a[0]));
        List<int[]> merged = new ArrayList<>();
        for (int[] iv : sorted) {
            if (!merged.isEmpty() && iv[0] <= merged.getLast()[1]) {
                merged.getLast()[1] = Math.max(merged.getLast()[1], iv[1]);
            } else {
                merged.add(new int[] {iv[0], iv[1]}); // a new pair, so extending it never changes the input
            }
        }
        return merged.toArray(new int[0][]);
    }

    public static void main(String[] args) {
        check(Arrays.deepEquals(mergeIntervals(new int[][] {{9, 12}, {1, 3}, {2, 6}, {8, 9}}), new int[][] {{1, 6}, {8, 12}}));
        check(Arrays.deepEquals(mergeIntervals(new int[][] {{1, 10}, {2, 3}, {4, 5}}), new int[][] {{1, 10}}));
        check(Arrays.deepEquals(mergeIntervals(new int[0][]), new int[0][]));
        check(Arrays.deepEquals(mergeIntervals(new int[][] {{5, 5}}), new int[][] {{5, 5}}));
        check(Arrays.deepEquals(mergeIntervals(new int[][] {{3, 4}, {1, 2}}), new int[][] {{1, 2}, {3, 4}}));
        check(Arrays.deepEquals(mergeIntervals(new int[][] {{4, 6}, {1, 4}, {6, 6}}), new int[][] {{1, 6}}));
        System.out.println("185 ok");
    }

    static void check(boolean ok) {
        if (!ok) throw new AssertionError("test failed");
    }
}
