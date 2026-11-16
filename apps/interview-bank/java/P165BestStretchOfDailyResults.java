// 165 · Best Stretch of Daily Results
public class P165BestStretchOfDailyResults {
    static int bestStretch(int[] results) {
        int endingHere = 0, best = results[0]; // endingHere: best stretch that ends on day r
        for (int r : results) {
            endingHere = Math.max(r, endingHere + r);
            best = Math.max(best, endingHere);
        }
        return best;
    }

    public static void main(String[] args) {
        check(bestStretch(new int[] {-2, 4, -1, 3, -7, 2}) == 6);
        check(bestStretch(new int[] {-8, -3, -5}) == -3);
        check(bestStretch(new int[] {5}) == 5);
        check(bestStretch(new int[] {1, 2, 3}) == 6);
        check(bestStretch(new int[] {-1, 0, -2}) == 0);
        check(bestStretch(new int[] {2, -1, 2, -9, 4}) == 4);
        check(bestStretch(new int[] {3, -2, 5, -1}) == 6);
        System.out.println("165 ok");
    }

    static void check(boolean ok) {
        if (!ok) throw new AssertionError("test failed");
    }
}
