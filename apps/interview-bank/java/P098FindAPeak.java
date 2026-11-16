// 098 · Find a Peak
public class P098FindAPeak {
    static int findPeak(int[] altitudes) {
        int lo = 0, hi = altitudes.length - 1; // the last index can be the answer, but has no right neighbour to test
        while (lo < hi) {
            int mid = lo + (hi - lo) / 2;
            // Going down after mid: there is a peak at mid or to its left.
            if (altitudes[mid] > altitudes[mid + 1]) hi = mid;
            else lo = mid + 1;
        }
        return lo;
    }

    static boolean isPeak(int[] a, int i) {
        return i >= 0 && i < a.length && (i == 0 || a[i - 1] < a[i]) && (i == a.length - 1 || a[i + 1] < a[i]);
    }

    public static void main(String[] args) {
        int[][] routes = {
            {120, 135, 150, 140, 160, 155},
            {10, 20, 30},
            {5},
            {3, 1},
            {1, 3},
            {5, 4, 3, 2, 1},
            {1, 3, 2, 4, 3, 5, 4},
            {1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 9},
        };
        for (int[] route : routes) check(isPeak(route, findPeak(route)));
        System.out.println("098 ok");
    }

    static void check(boolean ok) {
        if (!ok) throw new AssertionError("test failed");
    }
}
