// 092 · Where Would It Go?
public class P092WhereWouldItGo {
    static int insertPosition(int[] deadlines, int deadline) {
        int lo = 0, hi = deadlines.length; // the answer may be deadlines.length itself
        while (lo < hi) {
            int mid = lo + (hi - lo) / 2;
            if (deadlines[mid] >= deadline) hi = mid;
            else lo = mid + 1;
        }
        return lo;
    }

    public static void main(String[] args) {
        int[] stops = {480, 540, 600, 720};
        check(insertPosition(stops, 610) == 3);
        check(insertPosition(new int[] {480, 540, 540, 600}, 540) == 1);
        check(insertPosition(new int[0], 300) == 0);
        check(insertPosition(stops, 100) == 0);
        check(insertPosition(stops, 800) == 4);
        check(insertPosition(stops, 480) == 0);
        check(insertPosition(stops, 720) == 3);
        check(insertPosition(new int[] {5, 5, 5}, 5) == 0);
        System.out.println("092 ok");
    }

    static void check(boolean ok) {
        if (!ok) throw new AssertionError("test failed");
    }
}
