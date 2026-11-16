// 186 · Insert a New Interval
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class P186InsertANewInterval {
    static int[][] insertInterval(int[][] intervals, int[] newInterval) {
        List<int[]> result = new ArrayList<>();
        int start = newInterval[0], end = newInterval[1];
        int i = 0;
        while (i < intervals.length && intervals[i][1] < start) result.add(intervals[i++]);
        while (i < intervals.length && intervals[i][0] <= end) {
            start = Math.min(start, intervals[i][0]);
            end = Math.max(end, intervals[i][1]);
            i++;
        }
        result.add(new int[] {start, end});
        while (i < intervals.length) result.add(intervals[i++]);
        return result.toArray(new int[0][]);
    }

    public static void main(String[] args) {
        check(Arrays.deepEquals(
                insertInterval(new int[][] {{1, 3}, {6, 8}, {10, 12}, {15, 18}}, new int[] {7, 10}),
                new int[][] {{1, 3}, {6, 12}, {15, 18}}));
        check(Arrays.deepEquals(
                insertInterval(new int[][] {{2, 4}, {9, 11}}, new int[] {5, 7}), new int[][] {{2, 4}, {5, 7}, {9, 11}}));
        check(Arrays.deepEquals(insertInterval(new int[0][], new int[] {4, 6}), new int[][] {{4, 6}}));
        check(Arrays.deepEquals(insertInterval(new int[][] {{5, 6}}, new int[] {1, 2}), new int[][] {{1, 2}, {5, 6}}));
        check(Arrays.deepEquals(insertInterval(new int[][] {{1, 2}}, new int[] {7, 9}), new int[][] {{1, 2}, {7, 9}}));
        check(Arrays.deepEquals(insertInterval(new int[][] {{3, 4}, {6, 7}}, new int[] {1, 10}), new int[][] {{1, 10}}));
        check(Arrays.deepEquals(insertInterval(new int[][] {{1, 3}}, new int[] {3, 5}), new int[][] {{1, 5}}));
        check(Arrays.deepEquals(insertInterval(new int[][] {{5, 8}}, new int[] {2, 5}), new int[][] {{2, 8}}));
        System.out.println("186 ok");
    }

    static void check(boolean ok) {
        if (!ok) throw new AssertionError("test failed");
    }
}
