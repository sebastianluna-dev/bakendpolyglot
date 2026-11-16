// 181 · Can One Person Attend Every Meeting?
import java.util.Arrays;

public class P181CanOnePersonAttendEveryMeeting {
    static boolean canAttendAll(int[][] meetings) {
        Arrays.sort(meetings, (a, b) -> Integer.compare(a[0], b[0]));
        for (int i = 1; i < meetings.length; i++) {
            if (meetings[i][0] < meetings[i - 1][1]) return false; // starting just as the last one ends is fine
        }
        return true;
    }

    public static void main(String[] args) {
        check(canAttendAll(new int[][] {{540, 600}, {600, 660}, {480, 540}}));
        check(!canAttendAll(new int[][] {{540, 630}, {600, 660}}));
        check(canAttendAll(new int[0][]));
        check(canAttendAll(new int[][] {{100, 200}}));
        check(!canAttendAll(new int[][] {{700, 800}, {100, 200}, {150, 160}}));
        check(!canAttendAll(new int[][] {{5, 10}, {5, 6}}));
        check(canAttendAll(new int[][] {{0, 1}, {2, 3}, {1, 2}}));
        System.out.println("181 ok");
    }

    static void check(boolean ok) {
        if (!ok) throw new AssertionError("test failed");
    }
}
