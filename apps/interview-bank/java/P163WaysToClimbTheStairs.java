// 163 · Ways to Climb the Stairs
public class P163WaysToClimbTheStairs {
    static long waysToClimb(int n) {
        long prev = 1, cur = 1; // ways to reach steps i - 2 and i - 1
        for (int i = 2; i <= n; i++) {
            long next = prev + cur;
            prev = cur;
            cur = next;
        }
        return cur;
    }

    public static void main(String[] args) {
        check(waysToClimb(4) == 5);
        check(waysToClimb(1) == 1);
        check(waysToClimb(0) == 1);
        check(waysToClimb(2) == 2);
        check(waysToClimb(10) == 89);
        check(waysToClimb(46) == 2971215073L);
        check(waysToClimb(70) == 308061521170129L);
        System.out.println("163 ok");
    }

    static void check(boolean ok) {
        if (!ok) throw new AssertionError("test failed");
    }
}
