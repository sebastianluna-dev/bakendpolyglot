// 170 · Fewest Coins for an Amount
import java.util.Arrays;

public class P170FewestCoinsForAnAmount {
    static int fewestCoins(int[] coins, int amount) {
        int unreachable = amount + 1; // more coins than any real answer needs
        int[] fewest = new int[amount + 1];
        Arrays.fill(fewest, unreachable);
        fewest[0] = 0;
        for (int a = 1; a <= amount; a++) {
            for (int coin : coins) {
                if (coin <= a) fewest[a] = Math.min(fewest[a], fewest[a - coin] + 1);
            }
        }
        return fewest[amount] == unreachable ? -1 : fewest[amount];
    }

    public static void main(String[] args) {
        check(fewestCoins(new int[] {1, 4, 6}, 8) == 2);
        check(fewestCoins(new int[] {5, 10}, 3) == -1);
        check(fewestCoins(new int[] {7}, 0) == 0);
        check(fewestCoins(new int[] {2}, 3) == -1);
        check(fewestCoins(new int[] {3, 7}, 20) == 4);
        check(fewestCoins(new int[] {2, 5, 10, 25}, 99) == 7);
        check(fewestCoins(new int[] {4, 7}, 5000) == 716);
        System.out.println("170 ok");
    }

    static void check(boolean ok) {
        if (!ok) throw new AssertionError("test failed");
    }
}
