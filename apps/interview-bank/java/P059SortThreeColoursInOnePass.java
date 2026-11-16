// 059 · Sort Three Colours in One Pass
import java.util.Arrays;

public class P059SortThreeColoursInOnePass {
    static void sortColours(int[] stickers) {
        int low = 0, mid = 0, high = stickers.length - 1; // [0, low) is 0, [low, mid) is 1, (high, end] is 2
        while (mid <= high) {
            if (stickers[mid] == 0) {
                swap(stickers, low, mid);
                low++;
                mid++;
            } else if (stickers[mid] == 1) {
                mid++;
            } else {
                swap(stickers, mid, high);
                high--; // mid stays: the value swapped in has not been looked at yet
            }
        }
    }

    static void swap(int[] a, int i, int j) {
        int t = a[i];
        a[i] = a[j];
        a[j] = t;
    }

    static int[] sorted(int[] stickers) {
        sortColours(stickers);
        return stickers;
    }

    public static void main(String[] args) {
        check(Arrays.equals(sorted(new int[] {2, 0, 1, 2, 1, 0}), new int[] {0, 0, 1, 1, 2, 2}));
        check(Arrays.equals(sorted(new int[] {1, 1, 1}), new int[] {1, 1, 1}));
        check(Arrays.equals(sorted(new int[0]), new int[0]));
        check(Arrays.equals(sorted(new int[] {2, 0}), new int[] {0, 2}));
        check(Arrays.equals(sorted(new int[] {2, 2, 0, 0}), new int[] {0, 0, 2, 2}));
        check(Arrays.equals(sorted(new int[] {0, 2, 1}), new int[] {0, 1, 2}));
        check(Arrays.equals(sorted(new int[] {1, 2, 0}), new int[] {0, 1, 2}));
        check(Arrays.equals(sorted(new int[] {1, 2, 0, 2, 0, 1, 0}), new int[] {0, 0, 0, 1, 1, 2, 2}));
        System.out.println("059 ok");
    }

    static void check(boolean ok) {
        if (!ok) throw new AssertionError("test failed");
    }
}
