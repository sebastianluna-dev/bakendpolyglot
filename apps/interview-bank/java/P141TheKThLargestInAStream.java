// 141 · The k-th Largest in a Stream
import java.util.PriorityQueue;

public class P141TheKThLargestInAStream {
    static class KthLargest {
        private final int k;
        private final PriorityQueue<Integer> heap = new PriorityQueue<>(); // the k largest values so far; the smallest is on top

        KthLargest(int k, int[] nums) {
            this.k = k;
            for (int x : nums) add(x);
        }

        int add(int value) {
            heap.offer(value);
            if (heap.size() > k) heap.poll(); // drop the smallest: it is no longer in the top k
            return heap.peek();
        }
    }

    public static void main(String[] args) {
        KthLargest board = new KthLargest(3, new int[] {40, 75, 20, 90});
        check(board.add(50) == 50);
        check(board.add(10) == 50);
        check(board.add(80) == 75);
        check(board.add(95) == 80);

        KthLargest single = new KthLargest(1, new int[0]);
        check(single.add(-3) == -3);
        check(single.add(-5) == -3);
        check(single.add(2) == 2);

        KthLargest ties = new KthLargest(2, new int[] {7});
        check(ties.add(7) == 7);
        check(ties.add(3) == 7);
        check(ties.add(9) == 7);
        check(ties.add(8) == 8);

        KthLargest many = new KthLargest(4, new int[] {5, 1, 9, 3, 7, 2});
        check(many.add(4) == 4);
        check(many.add(6) == 5);
        check(many.add(10) == 6);
        System.out.println("141 ok");
    }

    static void check(boolean ok) {
        if (!ok) throw new AssertionError("test failed");
    }
}
