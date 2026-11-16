// 095 · Search a Rotated Sorted Array
public class P095SearchARotatedSortedArray {
    static int searchRotated(int[] times, int target) {
        int n = times.length;
        if (n == 0) return -1;
        int last = times[n - 1];
        int lo = 0, hi = n;
        while (lo < hi) {
            int mid = lo + (hi - lo) / 2;
            if (times[mid] <= last) hi = mid;
            else lo = mid + 1;
        }
        int start = lo; // index of the minimum: [start, n) and [0, start) are both sorted
        int from = target <= last ? start : 0;
        int to = target <= last ? n : start;
        int i = firstAtLeast(times, from, to, target);
        return i < to && times[i] == target ? i : -1;
    }

    static int firstAtLeast(int[] a, int lo, int hi, int target) {
        while (lo < hi) {
            int mid = lo + (hi - lo) / 2;
            if (a[mid] >= target) hi = mid;
            else lo = mid + 1;
        }
        return lo;
    }

    public static void main(String[] args) {
        int[] board = {900, 1020, 1200, 60, 300, 480};
        check(searchRotated(board, 300) == 4);
        check(searchRotated(board, 600) == -1);
        check(searchRotated(board, 1020) == 1);
        check(searchRotated(board, 900) == 0);
        check(searchRotated(board, 480) == 5);
        check(searchRotated(board, 60) == 3);
        check(searchRotated(board, 1300) == -1);
        check(searchRotated(board, 10) == -1);
        check(searchRotated(new int[] {1, 2, 3}, 3) == 2);
        check(searchRotated(new int[] {5}, 5) == 0);
        check(searchRotated(new int[] {5}, 1) == -1);
        check(searchRotated(new int[0], 1) == -1);
        check(searchRotated(new int[] {2, 1}, 1) == 1);
        check(searchRotated(new int[] {2, 1}, 2) == 0);
        System.out.println("095 ok");
    }

    static void check(boolean ok) {
        if (!ok) throw new AssertionError("test failed");
    }
}
