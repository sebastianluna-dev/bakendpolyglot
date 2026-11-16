// 193 · Cursor Pagination Over a Sorted List
import java.util.Arrays;
import java.util.Objects;

public class P193CursorPaginationOverASortedList {
    record Page(int[] items, Integer next) {}

    static Page fetchPage(int[] ids, Integer after, int limit) {
        int start = 0;
        if (after != null) {
            int lo = 0, hi = ids.length; // looking for the first id greater than after
            while (lo < hi) {
                int mid = lo + (hi - lo) / 2;
                if (ids[mid] > after) hi = mid;
                else lo = mid + 1;
            }
            start = lo;
        }
        int end = Math.min(start + limit, ids.length);
        int[] items = Arrays.copyOfRange(ids, start, end);
        return new Page(items, end < ids.length ? Integer.valueOf(items[items.length - 1]) : null);
    }

    static boolean is(Page page, int[] items, Integer next) {
        return Arrays.equals(page.items(), items) && Objects.equals(page.next(), next);
    }

    public static void main(String[] args) {
        int[] ids = {3, 8, 15, 21, 30, 42, 57};
        check(is(fetchPage(ids, null, 3), new int[] {3, 8, 15}, 15));
        check(is(fetchPage(ids, 15, 3), new int[] {21, 30, 42}, 42));
        check(is(fetchPage(ids, 42, 3), new int[] {57}, null));
        check(is(fetchPage(ids, 20, 3), new int[] {21, 30, 42}, 42));
        check(is(fetchPage(ids, 57, 3), new int[0], null));
        check(is(fetchPage(ids, 1, 10), new int[] {3, 8, 15, 21, 30, 42, 57}, null));
        check(is(fetchPage(new int[] {3, 8, 15, 21, 30, 42}, 15, 3), new int[] {21, 30, 42}, null));
        check(is(fetchPage(new int[0], null, 5), new int[0], null));
        System.out.println("193 ok");
    }

    static void check(boolean ok) {
        if (!ok) throw new AssertionError("test failed");
    }
}
