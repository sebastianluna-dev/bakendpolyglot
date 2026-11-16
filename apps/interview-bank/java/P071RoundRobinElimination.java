// 071 · Round-Robin Elimination
import java.util.ArrayDeque;
import java.util.Arrays;
import java.util.Deque;

public class P071RoundRobinElimination {
    static int[] eliminationOrder(int n, int k) {
        Deque<Integer> circle = new ArrayDeque<>();
        for (int i = 1; i <= n; i++) circle.offerLast(i);
        int[] order = new int[n];
        int out = 0;
        while (!circle.isEmpty()) {
            int steps = (k - 1) % circle.size(); // skip whole laps when k is larger than the circle
            for (int s = 0; s < steps; s++) circle.offerLast(circle.pollFirst());
            order[out++] = circle.pollFirst();
        }
        return order;
    }

    public static void main(String[] args) {
        check(Arrays.equals(eliminationOrder(5, 2), new int[] {2, 4, 1, 5, 3}));
        check(Arrays.equals(eliminationOrder(4, 3), new int[] {3, 2, 4, 1}));
        check(Arrays.equals(eliminationOrder(1, 7), new int[] {1}));
        check(Arrays.equals(eliminationOrder(4, 1), new int[] {1, 2, 3, 4}));
        check(Arrays.equals(eliminationOrder(3, 5), new int[] {2, 3, 1}));
        check(Arrays.equals(eliminationOrder(6, 6), new int[] {6, 1, 3, 2, 5, 4}));
        System.out.println("071 ok");
    }

    static void check(boolean ok) {
        if (!ok) throw new AssertionError("test failed");
    }
}
