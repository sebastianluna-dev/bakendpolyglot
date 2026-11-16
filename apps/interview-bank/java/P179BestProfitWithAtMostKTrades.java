// 179 · Best Profit With at Most k Trades
import java.util.Arrays;

public class P179BestProfitWithAtMostKTrades {
    static int bestProfit(int[] prices, int k) {
        if (prices.length == 0 || k == 0) return 0;
        int[] bought = new int[k + 1]; // bought[t]: most cash holding the t-th share
        Arrays.fill(bought, -prices[0]);
        int[] sold = new int[k + 1]; // sold[t]: most cash after t complete trades
        for (int p : prices) {
            for (int t = 1; t <= k; t++) {
                bought[t] = Math.max(bought[t], sold[t - 1] - p);
                sold[t] = Math.max(sold[t], bought[t] + p);
            }
        }
        return sold[k];
    }

    public static void main(String[] args) {
        int[] prices = {3, 8, 5, 1, 7, 8};
        int[] zigzag = {2, 5, 1, 4, 3, 6};
        check(bestProfit(prices, 2) == 12);
        check(bestProfit(prices, 1) == 7);
        check(bestProfit(new int[] {9, 7, 4, 1}, 2) == 0);
        check(bestProfit(prices, 0) == 0);
        check(bestProfit(new int[0], 3) == 0);
        check(bestProfit(new int[] {7}, 1) == 0);
        check(bestProfit(new int[] {1, 2, 3, 4, 5}, 2) == 4);
        check(bestProfit(zigzag, 1) == 5);
        check(bestProfit(zigzag, 2) == 8);
        check(bestProfit(zigzag, 3) == 9);
        check(bestProfit(zigzag, 10) == 9);
        check(bestProfit(new int[] {1, 3, 2, 8}, 1) == 7);
        System.out.println("179 ok");
    }

    static void check(boolean ok) {
        if (!ok) throw new AssertionError("test failed");
    }
}
