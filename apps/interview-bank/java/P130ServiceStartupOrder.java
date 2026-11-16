// 130 · Service Startup Order
import java.util.ArrayList;
import java.util.List;
import java.util.PriorityQueue;

public class P130ServiceStartupOrder {
    static List<Integer> startupOrder(int n, int[][] dependencies) {
        List<List<Integer>> dependents = new ArrayList<>();
        for (int i = 0; i < n; i++) dependents.add(new ArrayList<>());
        int[] waiting = new int[n]; // dependencies not yet started
        for (int[] d : dependencies) {
            dependents.get(d[1]).add(d[0]);
            waiting[d[0]]++;
        }
        PriorityQueue<Integer> ready = new PriorityQueue<>();
        for (int s = 0; s < n; s++) {
            if (waiting[s] == 0) ready.offer(s);
        }
        List<Integer> order = new ArrayList<>();
        while (!ready.isEmpty()) {
            int service = ready.poll();
            order.add(service);
            for (int d : dependents.get(service)) {
                waiting[d]--;
                if (waiting[d] == 0) ready.offer(d);
            }
        }
        return order.size() == n ? order : List.of();
    }

    public static void main(String[] args) {
        check(startupOrder(5, new int[][] {{0, 3}, {2, 4}, {1, 0}}).equals(List.of(3, 0, 1, 4, 2)));
        check(startupOrder(3, new int[][] {{0, 1}, {1, 2}, {2, 0}}).isEmpty());
        check(startupOrder(3, new int[0][]).equals(List.of(0, 1, 2)));
        check(startupOrder(1, new int[0][]).equals(List.of(0)));
        check(startupOrder(2, new int[][] {{1, 1}}).isEmpty());
        check(startupOrder(3, new int[][] {{2, 0}, {2, 0}, {1, 2}}).equals(List.of(0, 2, 1)));
        check(startupOrder(4, new int[][] {{1, 0}, {2, 3}, {3, 2}}).isEmpty());
        System.out.println("130 ok");
    }

    static void check(boolean ok) {
        if (!ok) throw new AssertionError("test failed");
    }
}
