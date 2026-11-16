// 091 · Binary Search
public class P091BinarySearch {
    static int binarySearch(int[] ids, int target) {
        int lo = 0, hi = ids.length; // the first ID >= target is somewhere in [lo, hi)
        while (lo < hi) {
            int mid = lo + (hi - lo) / 2;
            if (ids[mid] >= target) hi = mid;
            else lo = mid + 1;
        }
        return lo < ids.length && ids[lo] == target ? lo : -1;
    }

    public static void main(String[] args) {
        int[] ids = {3, 8, 15, 21, 40, 57};
        check(binarySearch(ids, 21) == 3);
        check(binarySearch(ids, 10) == -1);
        check(binarySearch(new int[0], 5) == -1);
        check(binarySearch(new int[] {7}, 7) == 0);
        check(binarySearch(ids, 3) == 0);
        check(binarySearch(ids, 57) == 5);
        check(binarySearch(ids, 1) == -1);
        check(binarySearch(ids, 99) == -1);
        System.out.println("091 ok");
    }

    static void check(boolean ok) {
        if (!ok) throw new AssertionError("test failed");
    }
}
