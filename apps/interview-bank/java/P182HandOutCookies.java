// 182 · Hand Out Cookies
import java.util.Arrays;

public class P182HandOutCookies {
    static int contentChildren(int[] greed, int[] sizes) {
        Arrays.sort(greed);
        Arrays.sort(sizes);
        int child = 0; // the least greedy child still without a cookie
        for (int size : sizes) {
            if (child < greed.length && size >= greed[child]) child++;
        }
        return child;
    }

    public static void main(String[] args) {
        check(contentChildren(new int[] {5, 2, 8, 2}, new int[] {3, 1, 6, 9}) == 3);
        check(contentChildren(new int[] {1, 3}, new int[] {3, 1}) == 2);
        check(contentChildren(new int[] {5, 2, 8, 2}, new int[] {3, 1, 6}) == 2);
        check(contentChildren(new int[] {4, 4}, new int[0]) == 0);
        check(contentChildren(new int[0], new int[] {1, 2}) == 0);
        check(contentChildren(new int[] {10, 20}, new int[] {1, 2, 3}) == 0);
        check(contentChildren(new int[] {2, 2, 2}, new int[] {2, 2}) == 2);
        check(contentChildren(new int[] {1, 2, 3}, new int[] {3}) == 1);
        System.out.println("182 ok");
    }

    static void check(boolean ok) {
        if (!ok) throw new AssertionError("test failed");
    }
}
