// 051 · Best Average Over k Days
public class P051BestAverageOverKDays {
    static double bestAverage(int[] balances, int k) {
        int sum = 0;
        for (int i = 0; i < k; i++) sum += balances[i];
        int best = sum;
        for (int i = k; i < balances.length; i++) {
            sum += balances[i] - balances[i - k]; // the day entering minus the day leaving
            best = Math.max(best, sum);
        }
        return (double) best / k;
    }

    public static void main(String[] args) {
        check(close(bestAverage(new int[] {4, -2, 7, 5, -1, 6}, 4), 4.25));
        check(close(bestAverage(new int[] {-3, -1, -4}, 2), -2.0));
        check(close(bestAverage(new int[] {5}, 1), 5.0));
        check(close(bestAverage(new int[] {1, 2, 3, 4}, 1), 4.0));
        check(close(bestAverage(new int[] {-7, -7, -7}, 3), -7.0));
        check(close(bestAverage(new int[] {1, 1, 2}, 3), 4.0 / 3));
        System.out.println("051 ok");
    }

    static boolean close(double got, double want) {
        return Math.abs(got - want) < 1e-9;
    }

    static void check(boolean ok) {
        if (!ok) throw new AssertionError("test failed");
    }
}
