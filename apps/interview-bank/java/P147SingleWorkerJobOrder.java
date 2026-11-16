// 147 · Single-Worker Job Order
import java.util.Arrays;
import java.util.Comparator;
import java.util.PriorityQueue;

public class P147SingleWorkerJobOrder {
    static int[] jobOrder(int[][] jobs) {
        int n = jobs.length;
        Integer[] byArrival = new Integer[n];
        for (int i = 0; i < n; i++) byArrival[i] = i;
        Arrays.sort(byArrival, Comparator.comparingInt(i -> jobs[i][0]));
        // job indices: shortest duration first, then the smaller index
        PriorityQueue<Integer> ready = new PriorityQueue<>(
                Comparator.<Integer>comparingInt(i -> jobs[i][1]).thenComparingInt(i -> i));
        int[] order = new int[n];
        long time = 0; // can pass Integer.MAX_VALUE
        int next = 0; // position in byArrival of the first job not yet queued
        for (int done = 0; done < n; done++) {
            if (ready.isEmpty() && time < jobs[byArrival[next]][0]) time = jobs[byArrival[next]][0]; // idle
            while (next < n && jobs[byArrival[next]][0] <= time) ready.offer(byArrival[next++]);
            int j = ready.poll();
            time += jobs[j][1];
            order[done] = j;
        }
        return order;
    }

    public static void main(String[] args) {
        check(Arrays.equals(jobOrder(new int[][] {{1, 3}, {2, 1}, {3, 2}, {4, 1}}), new int[] {0, 1, 3, 2}));
        check(Arrays.equals(jobOrder(new int[][] {{5, 2}, {5, 2}, {5, 1}, {20, 1}}), new int[] {2, 0, 1, 3}));
        check(Arrays.equals(jobOrder(new int[][] {{7, 4}}), new int[] {0}));
        check(Arrays.equals(jobOrder(new int[][] {{1, 2}, {3, 5}, {3, 1}}), new int[] {0, 2, 1}));
        check(Arrays.equals(jobOrder(new int[][] {{9, 1}, {1, 1}}), new int[] {1, 0}));
        check(Arrays.equals(jobOrder(new int[][] {{1, 1000000000}, {1, 1000000000}, {1000000000, 1}}),
                new int[] {0, 2, 1}));
        System.out.println("147 ok");
    }

    static void check(boolean ok) {
        if (!ok) throw new AssertionError("test failed");
    }
}
