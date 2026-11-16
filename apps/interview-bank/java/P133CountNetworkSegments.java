// 133 · Count Network Segments
public class P133CountNetworkSegments {
    static int countSegments(int n, int[][] links) {
        int[] parent = new int[n];
        int[] size = new int[n];
        for (int i = 0; i < n; i++) {
            parent[i] = i;
            size[i] = 1;
        }
        int segments = n;
        for (int[] link : links) {
            int ra = find(parent, link[0]);
            int rb = find(parent, link[1]);
            if (ra == rb) continue;
            if (size[ra] < size[rb]) {
                int t = ra;
                ra = rb;
                rb = t;
            }
            parent[rb] = ra; // the smaller tree goes under the larger one
            size[ra] += size[rb];
            segments--;
        }
        return segments;
    }

    static int find(int[] parent, int x) {
        while (parent[x] != x) {
            parent[x] = parent[parent[x]]; // path halving: point x at its grandparent
            x = parent[x];
        }
        return x;
    }

    public static void main(String[] args) {
        check(countSegments(5, new int[][] {{0, 1}, {1, 2}, {3, 4}}) == 2);
        check(countSegments(6, new int[][] {{0, 1}, {2, 3}, {1, 0}}) == 4);
        check(countSegments(1, new int[0][]) == 1);
        check(countSegments(3, new int[0][]) == 3);
        check(countSegments(3, new int[][] {{0, 1}, {1, 2}, {2, 0}}) == 1);
        check(countSegments(2, new int[][] {{1, 1}}) == 2);
        check(countSegments(6, new int[][] {{0, 1}, {2, 3}, {4, 5}, {1, 3}, {5, 0}}) == 1);
        System.out.println("133 ok");
    }

    static void check(boolean ok) {
        if (!ok) throw new AssertionError("test failed");
    }
}
