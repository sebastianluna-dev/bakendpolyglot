// 008 · Best Single Trade
public class P008BestSingleTrade {
    static int bestTrade(int[] prices) {
        int lowest = Integer.MAX_VALUE;
        int best = 0;
        for (int price : prices) {
            lowest = Math.min(lowest, price);
            best = Math.max(best, price - lowest);
        }
        return best;
    }

    public static void main(String[] args) {
        check(bestTrade(new int[] {8, 3, 6, 1, 7, 4}) == 6);
        check(bestTrade(new int[] {9, 7, 4, 2}) == 0);
        check(bestTrade(new int[] {5, 9, 1}) == 4);
        check(bestTrade(new int[] {3}) == 0);
        check(bestTrade(new int[0]) == 0);
        System.out.println("008 ok");
    }

    static void check(boolean ok) {
        if (!ok) throw new AssertionError("test failed");
    }
}
