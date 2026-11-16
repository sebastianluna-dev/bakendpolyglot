// 002 · Two Prices for a Budget
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public class P002TwoPrices {
    static int[] twoPrices(int[] prices, int budget) {
        Map<Integer, Integer> firstIndex = new HashMap<>(); // price -> earliest index
        for (int j = 0; j < prices.length; j++) {
            Integer i = firstIndex.get(budget - prices[j]);
            if (i != null) return new int[] {i, j};
            firstIndex.putIfAbsent(prices[j], j);
        }
        return new int[0];
    }

    public static void main(String[] args) {
        check(Arrays.equals(twoPrices(new int[] {12, 7, 3, 9, 5}, 12), new int[] {2, 3}));
        check(Arrays.equals(twoPrices(new int[] {4, 4, 1}, 8), new int[] {0, 1}));
        check(Arrays.equals(twoPrices(new int[] {1, 2}, 10), new int[0]));
        System.out.println("002 ok");
    }

    static void check(boolean ok) {
        if (!ok) throw new AssertionError("test failed");
    }
}
