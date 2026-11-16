// 047 · Merge Two Sorted Arrays in Place
import java.util.Arrays;

public class P047MergeTwoSortedArraysInPlace {
    static void mergeInPlace(int[] a, int m, int[] b) {
        int i = m - 1, j = b.length - 1;
        int k = m + b.length - 1; // next slot to fill, from the back
        while (j >= 0) {
            if (i >= 0 && a[i] > b[j]) a[k--] = a[i--];
            else a[k--] = b[j--];
        }
    }

    static int[] merged(int[] a, int m, int[] b) {
        mergeInPlace(a, m, b);
        return a;
    }

    public static void main(String[] args) {
        check(Arrays.equals(merged(new int[] {1, 4, 7, 0, 0}, 3, new int[] {2, 8}), new int[] {1, 2, 4, 7, 8}));
        check(Arrays.equals(merged(new int[] {0, 0}, 0, new int[] {3, 5}), new int[] {3, 5}));
        check(Arrays.equals(merged(new int[] {2, 3}, 2, new int[0]), new int[] {2, 3}));
        check(Arrays.equals(merged(new int[] {1, 2, 2, 0, 0, 0}, 3, new int[] {2, 2, 3}), new int[] {1, 2, 2, 2, 2, 3}));
        check(Arrays.equals(merged(new int[] {-3, 5, 0}, 2, new int[] {-4}), new int[] {-4, -3, 5}));
        check(Arrays.equals(merged(new int[] {4, 5, 0}, 2, new int[] {1}), new int[] {1, 4, 5}));
        System.out.println("047 ok");
    }

    static void check(boolean ok) {
        if (!ok) throw new AssertionError("test failed");
    }
}
