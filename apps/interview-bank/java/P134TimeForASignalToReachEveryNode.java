// 134 · Time for a Signal to Reach Every Node
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.PriorityQueue;

public class P134TimeForASignalToReachEveryNode {
    static int signalTime(int n, int[][] links, int source) {
        List<List<int[]>> graph = new ArrayList<>();
        for (int i = 0; i < n; i++) graph.add(new ArrayList<>());
        for (int[] link : links) graph.get(link[0]).add(new int[] {link[1], link[2]});
        int[] dist = new int[n];
        Arrays.fill(dist, Integer.MAX_VALUE);
        dist[source] = 0;
        PriorityQueue<int[]> heap = new PriorityQueue<>(Comparator.comparingInt(e -> e[0])); // {dist, node}
        heap.offer(new int[] {0, source});
        while (!heap.isEmpty()) {
            int[] top = heap.poll();
            int d = top[0], node = top[1];
            if (d > dist[node]) continue; // stale: a shorter path was found after this was queued
            for (int[] edge : graph.get(node)) {
                int next = edge[0], nd = d + edge[1];
                if (nd < dist[next]) {
                    dist[next] = nd;
                    heap.offer(new int[] {nd, next});
                }
            }
        }
        int slowest = Arrays.stream(dist).max().getAsInt();
        return slowest == Integer.MAX_VALUE ? -1 : slowest;
    }

    public static void main(String[] args) {
        check(signalTime(4, new int[][] {{0, 1, 4}, {0, 2, 1}, {2, 1, 2}, {1, 3, 1}, {2, 3, 6}}, 0) == 4);
        check(signalTime(3, new int[][] {{0, 1, 2}, {2, 1, 1}}, 0) == -1);
        check(signalTime(1, new int[0][], 0) == 0);
        check(signalTime(2, new int[][] {{0, 1, 5}, {0, 1, 2}}, 0) == 2);
        check(signalTime(2, new int[][] {{0, 1, 3}}, 1) == -1);
        check(signalTime(2, new int[][] {{1, 0, 3}}, 1) == 3);
        check(signalTime(3, new int[][] {{0, 1, 1}, {1, 2, 1}, {2, 0, 1}}, 1) == 2);
        check(signalTime(3, new int[][] {{0, 1, 0}, {1, 2, 0}}, 0) == 0);
        System.out.println("134 ok");
    }

    static void check(boolean ok) {
        if (!ok) throw new AssertionError("test failed");
    }
}
