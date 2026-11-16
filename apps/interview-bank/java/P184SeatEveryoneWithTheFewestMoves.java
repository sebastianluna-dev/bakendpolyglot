// 184 · Seat Everyone With the Fewest Moves
import java.util.Arrays;

public class P184SeatEveryoneWithTheFewestMoves {
    static int fewestMoves(int[] seats, int[] guests) {
        int[] s = seats.clone();
        int[] g = guests.clone();
        Arrays.sort(s);
        Arrays.sort(g);
        int moves = 0;
        for (int i = 0; i < s.length; i++) moves += Math.abs(s[i] - g[i]);
        return moves;
    }

    public static void main(String[] args) {
        check(fewestMoves(new int[] {4, 1, 7}, new int[] {2, 8, 3}) == 3);
        check(fewestMoves(new int[] {2, 6, 9, 10}, new int[] {5, 5, 1, 12}) == 8);
        check(fewestMoves(new int[0], new int[0]) == 0);
        check(fewestMoves(new int[] {3, 5}, new int[] {5, 3}) == 0);
        check(fewestMoves(new int[] {10}, new int[] {1}) == 9);
        System.out.println("184 ok");
    }

    static void check(boolean ok) {
        if (!ok) throw new AssertionError("test failed");
    }
}
