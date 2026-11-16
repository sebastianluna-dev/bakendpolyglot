// 164 · Rob Houses Without Tripping the Alarm
public class P164RobHousesWithoutTrippingTheAlarm {
    static int maxHaul(int[] cash) {
        int skipPrev = 0, best = 0; // best totals up to two houses back and up to here
        for (int c : cash) {
            int next = Math.max(best, skipPrev + c);
            skipPrev = best;
            best = next;
        }
        return best;
    }

    public static void main(String[] args) {
        check(maxHaul(new int[] {6, 1, 2, 9}) == 15);
        check(maxHaul(new int[] {5, 10, 4, 3, 8}) == 18);
        check(maxHaul(new int[0]) == 0);
        check(maxHaul(new int[] {7}) == 7);
        check(maxHaul(new int[] {2, 1, 1, 2}) == 4);
        check(maxHaul(new int[] {4, 1, 2, 7, 5, 3, 1}) == 14);
        check(maxHaul(new int[] {0, 0, 0}) == 0);
        System.out.println("164 ok");
    }

    static void check(boolean ok) {
        if (!ok) throw new AssertionError("test failed");
    }
}
