// 006 · The Unpaired Seat
public class P006TheUnpairedSeat {
    static int unpairedSeat(int[] log) {
        int result = 0;
        for (int seat : log) result ^= seat; // pairs cancel: x ^ x is 0
        return result;
    }

    public static void main(String[] args) {
        check(unpairedSeat(new int[] {14, 3, 14, 8, 3}) == 8);
        check(unpairedSeat(new int[] {21}) == 21);
        check(unpairedSeat(new int[] {4, 4, 9}) == 9);
        check(unpairedSeat(new int[] {7, 2, 2}) == 7);
        check(unpairedSeat(new int[] {1000000000, 5, 5}) == 1000000000);
        System.out.println("006 ok");
    }

    static void check(boolean ok) {
        if (!ok) throw new AssertionError("test failed");
    }
}
