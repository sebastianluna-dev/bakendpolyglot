// 101 · Median of Two Sorted Arrays

public class P101MedianOfTwoSortedArrays {
    static double medianOfTwoSorted(int[] a, int[] b) {
        if (a.length > b.length) return medianOfTwoSorted(b, a);
        int m = a.length, n = b.length;
        int half = (m + n + 1) / 2;
        int lo = 0, hi = m; // m stands for "take all of a"
        while (lo < hi) {
            int mid = lo + (hi - lo) / 2; // values of a in the left half
            int rest = half - mid;
            if (rest == 0 || b[rest - 1] <= a[mid]) hi = mid;
            else lo = mid + 1;
        }
        int i = lo;
        int j = half - i;
        int leftMax = Math.max(i > 0 ? a[i - 1] : Integer.MIN_VALUE, j > 0 ? b[j - 1] : Integer.MIN_VALUE);
        if ((m + n) % 2 == 1) return leftMax;
        int rightMin = Math.min(i < m ? a[i] : Integer.MAX_VALUE, j < n ? b[j] : Integer.MAX_VALUE);
        return (leftMax + rightMin) / 2.0;
    }

    public static void main(String[] args) {
        check(medianOfTwoSorted(new int[] {12, 18, 31}, new int[] {9, 20, 25, 40}) == 20);
        check(medianOfTwoSorted(new int[] {5, 9}, new int[] {6, 30}) == 7.5);
        check(medianOfTwoSorted(new int[0], new int[] {4}) == 4);
        check(medianOfTwoSorted(new int[] {1, 2}, new int[0]) == 1.5);
        check(medianOfTwoSorted(new int[] {2, 2, 2}, new int[] {2, 2}) == 2);
        check(medianOfTwoSorted(new int[] {1, 2}, new int[] {3, 4, 5, 6}) == 3.5);
        check(medianOfTwoSorted(new int[] {1, 3, 5, 7, 9}, new int[] {2}) == 4);
        check(medianOfTwoSorted(new int[] {-5, -1}, new int[] {-3}) == -3);
        check(medianOfTwoSorted(new int[] {10}, new int[] {1, 2, 3}) == 2.5);
        System.out.println("101 ok");
    }

    static void check(boolean ok) {
        if (!ok) throw new AssertionError("test failed");
    }
}
