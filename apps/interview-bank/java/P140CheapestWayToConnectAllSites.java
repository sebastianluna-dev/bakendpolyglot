// 140 · Cheapest Way to Connect All Sites
import java.util.Arrays;
import java.util.Comparator;

public class P140CheapestWayToConnectAllSites {
    static int cheapestConnection(int n, int[][] cables) {
        int[][] sorted = cables.clone();
        Arrays.sort(sorted, Comparator.comparingInt(c -> c[2]));
        int[] parent = new int[n];
        int[] size = new int[n];
        for (int i = 0; i < n; i++) {
            parent[i] = i;
            size[i] = 1;
        }
        int total = 0, used = 0;
        for (int[] cable : sorted) {
            int ra = find(parent, cable[0]);
            int rb = find(parent, cable[1]);
            if (ra == rb) continue; // both ends already connected: this cable would close a loop
            if (size[ra] < size[rb]) {
                int t = ra;
                ra = rb;
                rb = t;
            }
            parent[rb] = ra;
            size[ra] += size[rb];
            total += cable[2];
            used++;
        }
        return used == n - 1 ? total : -1;
    }

    static int find(int[] parent, int x) {
        while (parent[x] != x) {
            parent[x] = parent[parent[x]]; // path halving: point x at its grandparent
            x = parent[x];
        }
        return x;
    }

    public static void main(String[] args) {
        check(cheapestConnection(4, new int[][] {{0, 1, 3}, {1, 2, 1}, {2, 3, 4}, {0, 2, 2}, {1, 3, 5}}) == 7);
        check(cheapestConnection(3, new int[][] {{0, 1, 5}}) == -1);
        check(cheapestConnection(1, new int[0][]) == 0);
        check(cheapestConnection(2, new int[0][]) == -1);
        check(cheapestConnection(2, new int[][] {{0, 1, 9}, {1, 0, 4}, {0, 0, 1}}) == 4);
        check(cheapestConnection(5, new int[][] {{0, 1, 2}, {1, 2, 2}, {2, 0, 2}, {3, 4, 1}, {2, 3, 7}, {0, 4, 8}}) == 12);
        System.out.println("140 ok");
    }

    static void check(boolean ok) {
        if (!ok) throw new AssertionError("test failed");
    }
}
