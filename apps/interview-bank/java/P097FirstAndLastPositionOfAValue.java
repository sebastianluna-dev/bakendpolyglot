// 097 · First and Last Position of a Value
import java.util.Arrays;

public class P097FirstAndLastPositionOfAValue {
    static int[] firstAndLast(int[] days, int day) {
        int first = firstAtLeast(days, day);
        if (first == days.length || days[first] != day) return new int[] {-1, -1};
        return new int[] {first, firstAtLeast(days, day + 1) - 1}; // one before the first value >= day + 1
    }

    static int firstAtLeast(int[] a, int target) {
        int lo = 0, hi = a.length;
        while (lo < hi) {
            int mid = lo + (hi - lo) / 2;
            if (a[mid] >= target) hi = mid;
            else lo = mid + 1;
        }
        return lo;
    }

    public static void main(String[] args) {
        int[] due = {3, 5, 5, 5, 8, 9};
        int[] none = {-1, -1};
        check(Arrays.equals(firstAndLast(due, 5), new int[] {1, 3}));
        check(Arrays.equals(firstAndLast(new int[] {3, 5, 8}, 6), none));
        check(Arrays.equals(firstAndLast(new int[0], 1), none));
        check(Arrays.equals(firstAndLast(new int[] {7, 7, 7, 7}, 7), new int[] {0, 3}));
        check(Arrays.equals(firstAndLast(new int[] {1}, 1), new int[] {0, 0}));
        check(Arrays.equals(firstAndLast(new int[] {1, 2, 2}, 2), new int[] {1, 2}));
        check(Arrays.equals(firstAndLast(due, 1), none));
        check(Arrays.equals(firstAndLast(due, 10), none));
        check(Arrays.equals(firstAndLast(due, 3), new int[] {0, 0}));
        check(Arrays.equals(firstAndLast(due, 9), new int[] {5, 5}));
        System.out.println("097 ok");
    }

    static void check(boolean ok) {
        if (!ok) throw new AssertionError("test failed");
    }
}
