// 058 · Shortest Stretch Reaching a Total
public class P058ShortestStretchReachingATotal {
    static int shortestReaching(int[] weights, int target) {
        int n = weights.length;
        int best = n + 1; // longer than any real stretch: means "none found"
        int left = 0, sum = 0;
        for (int right = 0; right < n; right++) {
            sum += weights[right];
            while (sum >= target) {
                best = Math.min(best, right - left + 1);
                sum -= weights[left];
                left++;
            }
        }
        return best == n + 1 ? 0 : best;
    }

    public static void main(String[] args) {
        check(shortestReaching(new int[] {3, 1, 4, 1, 5, 9, 2}, 15) == 3);
        check(shortestReaching(new int[] {2, 2, 2}, 7) == 0);
        check(shortestReaching(new int[0], 5) == 0);
        check(shortestReaching(new int[] {8}, 8) == 1);
        check(shortestReaching(new int[] {1, 1, 1, 1}, 4) == 4);
        check(shortestReaching(new int[] {1, 6, 1, 1, 7}, 7) == 1);
        System.out.println("058 ok");
    }

    static void check(boolean ok) {
        if (!ok) throw new AssertionError("test failed");
    }
}
