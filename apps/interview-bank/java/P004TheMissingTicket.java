// 004 · The Missing Ticket
import java.util.stream.IntStream;

public class P004TheMissingTicket {
    static int missingTicket(int[] scanned) {
        long n = scanned.length + 1;
        long total = n * (n + 1) / 2; // long: n * (n + 1) overflows int above 46,340 tickets
        for (int t : scanned) total -= t;
        return (int) total;
    }

    public static void main(String[] args) {
        int[] many = IntStream.rangeClosed(1, 70000).filter(t -> t != 31415).toArray();

        check(missingTicket(new int[] {3, 1, 4, 5}) == 2);
        check(missingTicket(new int[] {2}) == 1);
        check(missingTicket(new int[] {1}) == 2);
        check(missingTicket(new int[0]) == 1);
        check(missingTicket(many) == 31415);
        System.out.println("004 ok");
    }

    static void check(boolean ok) {
        if (!ok) throw new AssertionError("test failed");
    }
}
