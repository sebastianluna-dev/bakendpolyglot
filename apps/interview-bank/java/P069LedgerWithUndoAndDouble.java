// 069 · Ledger With Undo and Double
import java.util.ArrayList;
import java.util.List;

public class P069LedgerWithUndoAndDouble {
    static int ledgerTotal(String[] commands) {
        List<Integer> amounts = new ArrayList<>();
        for (String cmd : commands) {
            int n = amounts.size();
            switch (cmd) {
                case "U" -> amounts.remove(n - 1);
                case "D" -> amounts.add(2 * amounts.get(n - 1));
                case "+" -> amounts.add(amounts.get(n - 1) + amounts.get(n - 2));
                default -> amounts.add(Integer.parseInt(cmd));
            }
        }
        int total = 0;
        for (int x : amounts) total += x;
        return total;
    }

    public static void main(String[] args) {
        check(ledgerTotal(new String[] {"5", "-2", "D", "+", "U", "10", "+"}) == 15);
        check(ledgerTotal(new String[] {"7", "U"}) == 0);
        check(ledgerTotal(new String[0]) == 0);
        check(ledgerTotal(new String[] {"100"}) == 100);
        check(ledgerTotal(new String[] {"2", "3", "+"}) == 10);
        check(ledgerTotal(new String[] {"3", "D", "D", "U", "+"}) == 18);
        check(ledgerTotal(new String[] {"-4", "D", "U", "U"}) == 0);
        System.out.println("069 ok");
    }

    static void check(boolean ok) {
        if (!ok) throw new AssertionError("test failed");
    }
}
