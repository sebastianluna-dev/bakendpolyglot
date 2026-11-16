// 139 · Cheapest Route With at Most k Stops
import java.util.Arrays;

public class P139CheapestRouteWithAtMostKStops {
    static int cheapestRoute(int n, int[][] legs, int source, int target, int k) {
        int[] cost = new int[n];
        Arrays.fill(cost, Integer.MAX_VALUE);
        cost[source] = 0;
        for (int round = 0; round <= k; round++) {
            int[] next = cost.clone(); // relax from last round's costs only, so each round adds at most one leg
            for (int[] leg : legs) {
                int from = leg[0], to = leg[1], price = leg[2];
                if (cost[from] != Integer.MAX_VALUE && cost[from] + price < next[to]) next[to] = cost[from] + price;
            }
            cost = next;
        }
        return cost[target] == Integer.MAX_VALUE ? -1 : cost[target];
    }

    public static void main(String[] args) {
        int[][] legs = {{0, 1, 100}, {1, 2, 100}, {2, 3, 100}, {0, 2, 500}, {1, 3, 600}};
        int[][] triangle = {{0, 1, 1}, {1, 2, 1}, {0, 2, 5}};
        check(cheapestRoute(4, legs, 0, 3, 1) == 600);
        check(cheapestRoute(4, legs, 0, 3, 2) == 300);
        check(cheapestRoute(4, legs, 0, 3, 0) == -1);
        check(cheapestRoute(4, legs, 0, 3, 10) == 300);
        check(cheapestRoute(3, triangle, 0, 2, 0) == 5);
        check(cheapestRoute(3, triangle, 0, 2, 1) == 2);
        check(cheapestRoute(2, new int[][] {{0, 1, 7}}, 1, 1, 0) == 0);
        check(cheapestRoute(3, new int[][] {{0, 1, 4}}, 0, 2, 5) == -1);
        check(cheapestRoute(2, new int[][] {{1, 0, 3}}, 0, 1, 3) == -1);
        System.out.println("139 ok");
    }

    static void check(boolean ok) {
        if (!ok) throw new AssertionError("test failed");
    }
}
