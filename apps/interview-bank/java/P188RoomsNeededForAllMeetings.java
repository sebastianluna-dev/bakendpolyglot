// 188 · Rooms Needed for All Meetings
import java.util.Arrays;

public class P188RoomsNeededForAllMeetings {
    static int roomsNeeded(int[][] meetings) {
        int[] starts = Arrays.stream(meetings).mapToInt(m -> m[0]).sorted().toArray();
        int[] ends = Arrays.stream(meetings).mapToInt(m -> m[1]).sorted().toArray();
        int rooms = 0, j = 0; // ends[j] is the earliest end that has not freed a room yet
        for (int start : starts) {
            if (start >= ends[j]) {
                j++; // a meeting is over: reuse its room
            } else {
                rooms++;
            }
        }
        return rooms;
    }

    public static void main(String[] args) {
        check(roomsNeeded(new int[][] {{9, 12}, {10, 11}, {11, 13}, {13, 14}}) == 2);
        check(roomsNeeded(new int[][] {{8, 10}, {10, 12}, {12, 14}}) == 1);
        check(roomsNeeded(new int[0][]) == 0);
        check(roomsNeeded(new int[][] {{1, 5}, {2, 6}, {3, 7}}) == 3);
        check(roomsNeeded(new int[][] {{2, 3}, {2, 3}}) == 2);
        check(roomsNeeded(new int[][] {{5, 10}, {1, 4}, {4, 6}}) == 2);
        System.out.println("188 ok");
    }

    static void check(boolean ok) {
        if (!ok) throw new AssertionError("test failed");
    }
}
