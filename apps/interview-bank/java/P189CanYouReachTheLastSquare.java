// 189 · Can You Reach the Last Square?
public class P189CanYouReachTheLastSquare {
    static boolean canReachLast(int[] jumps) {
        int furthest = 0;
        for (int i = 0; i < jumps.length; i++) {
            if (i > furthest) return false; // no reachable square jumps this far
            furthest = Math.max(furthest, i + jumps[i]);
        }
        return true;
    }

    public static void main(String[] args) {
        check(canReachLast(new int[] {2, 0, 3, 0, 0, 1}));
        check(!canReachLast(new int[] {3, 1, 1, 0, 4}));
        check(canReachLast(new int[] {0}));
        check(!canReachLast(new int[] {0, 1}));
        check(canReachLast(new int[] {4, 0, 0, 0, 0}));
        check(!canReachLast(new int[] {1, 1, 0, 1}));
        check(canReachLast(new int[] {1, 2, 0, 0}));
        System.out.println("189 ok");
    }

    static void check(boolean ok) {
        if (!ok) throw new AssertionError("test failed");
    }
}
