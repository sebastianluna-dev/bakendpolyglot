// 142 · Smash the Heaviest Stones
import java.util.Comparator;
import java.util.PriorityQueue;

public class P142SmashTheHeaviestStones {
    static int lastStone(int[] stones) {
        PriorityQueue<Integer> heap = new PriorityQueue<>(Comparator.reverseOrder()); // heaviest first
        for (int s : stones) heap.offer(s);
        while (heap.size() > 1) {
            int x = heap.poll();
            int y = heap.poll();
            if (x > y) heap.offer(x - y);
        }
        return heap.isEmpty() ? 0 : heap.peek();
    }

    public static void main(String[] args) {
        check(lastStone(new int[] {3, 9, 4, 4, 1}) == 1);
        check(lastStone(new int[] {10, 4, 4, 2}) == 0);
        check(lastStone(new int[] {6, 6}) == 0);
        check(lastStone(new int[] {7}) == 7);
        check(lastStone(new int[] {1, 1, 1}) == 1);
        check(lastStone(new int[] {2, 2, 3}) == 1);
        check(lastStone(new int[] {5, 1, 1, 1, 1, 1}) == 0);
        System.out.println("142 ok");
    }

    static void check(boolean ok) {
        if (!ok) throw new AssertionError("test failed");
    }
}
