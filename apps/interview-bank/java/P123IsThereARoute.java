// 123 · Is There a Route?
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Deque;
import java.util.List;

public class P123IsThereARoute {
    static boolean hasRoute(int n, int[][] edges, int source, int target) {
        List<List<Integer>> graph = new ArrayList<>();
        for (int i = 0; i < n; i++) graph.add(new ArrayList<>());
        for (int[] e : edges) {
            graph.get(e[0]).add(e[1]);
            graph.get(e[1]).add(e[0]);
        }
        boolean[] seen = new boolean[n];
        seen[source] = true;
        Deque<Integer> queue = new ArrayDeque<>();
        queue.offer(source);
        while (!queue.isEmpty()) {
            int node = queue.poll();
            if (node == target) return true;
            for (int next : graph.get(node)) {
                if (seen[next]) continue;
                seen[next] = true; // mark when queued, so no node is queued twice
                queue.offer(next);
            }
        }
        return false;
    }

    public static void main(String[] args) {
        int[][] roads = {{0, 1}, {1, 3}, {3, 2}, {4, 5}};
        check(hasRoute(6, roads, 0, 2));
        check(!hasRoute(6, roads, 0, 5));
        check(hasRoute(1, new int[0][], 0, 0));
        check(!hasRoute(3, new int[0][], 0, 2));
        check(hasRoute(2, new int[][] {{1, 0}}, 0, 1));
        check(hasRoute(4, new int[][] {{0, 1}, {1, 0}, {1, 2}, {2, 0}, {2, 3}}, 3, 0));
        System.out.println("123 ok");
    }

    static void check(boolean ok) {
        if (!ok) throw new AssertionError("test failed");
    }
}
