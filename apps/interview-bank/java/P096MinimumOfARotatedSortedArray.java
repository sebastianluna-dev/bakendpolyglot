// 096 · Minimum of a Rotated Sorted Array
public class P096MinimumOfARotatedSortedArray {
    static int rotatedMinimum(int[] times) {
        int last = times[times.length - 1];
        int lo = 0, hi = times.length;
        while (lo < hi) {
            int mid = lo + (hi - lo) / 2;
            // Values <= last belong to the run after the wrap, which starts at the minimum.
            if (times[mid] <= last) hi = mid;
            else lo = mid + 1;
        }
        return times[lo];
    }

    public static void main(String[] args) {
        check(rotatedMinimum(new int[] {900, 1020, 1200, 60, 300, 480}) == 60);
        check(rotatedMinimum(new int[] {60, 300, 480}) == 60);
        check(rotatedMinimum(new int[] {7}) == 7);
        check(rotatedMinimum(new int[] {2, 1}) == 1);
        check(rotatedMinimum(new int[] {3, 4, 5, 1, 2}) == 1);
        check(rotatedMinimum(new int[] {2, 3, 4, 5, 1}) == 1);
        check(rotatedMinimum(new int[] {5, 1, 2, 3, 4}) == 1);
        System.out.println("096 ok");
    }

    static void check(boolean ok) {
        if (!ok) throw new AssertionError("test failed");
    }
}
