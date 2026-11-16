// 009 · Rotate the Carousel
import java.util.Arrays;

public class P009RotateTheCarousel {
    static void rotateCarousel(int[] slides, int k) {
        int n = slides.length;
        if (n == 0) return;
        k %= n;
        reverse(slides, 0, n - 1);
        reverse(slides, 0, k - 1);
        reverse(slides, k, n - 1);
    }

    static void reverse(int[] a, int lo, int hi) {
        for (; lo < hi; lo++, hi--) {
            int tmp = a[lo];
            a[lo] = a[hi];
            a[hi] = tmp;
        }
    }

    static int[] rotated(int[] slides, int k) {
        rotateCarousel(slides, k);
        return slides;
    }

    public static void main(String[] args) {
        check(Arrays.equals(rotated(new int[] {1, 2, 3, 4, 5}, 2), new int[] {4, 5, 1, 2, 3}));
        check(Arrays.equals(rotated(new int[] {10, 20}, 5), new int[] {20, 10}));
        check(Arrays.equals(rotated(new int[] {1, 2, 3}, 0), new int[] {1, 2, 3}));
        check(Arrays.equals(rotated(new int[] {1, 2, 3}, 3), new int[] {1, 2, 3}));
        check(Arrays.equals(rotated(new int[] {7}, 4), new int[] {7}));
        check(Arrays.equals(rotated(new int[0], 3), new int[0]));
        System.out.println("009 ok");
    }

    static void check(boolean ok) {
        if (!ok) throw new AssertionError("test failed");
    }
}
