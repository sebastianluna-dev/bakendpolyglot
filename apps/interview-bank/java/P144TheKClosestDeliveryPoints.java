// 144 · The k Closest Delivery Points
import java.util.Arrays;
import java.util.Comparator;
import java.util.PriorityQueue;

public class P144TheKClosestDeliveryPoints {
    static int[][] kClosest(int[][] points, int k) {
        Comparator<int[]> closer = Comparator.<int[]>comparingInt(p -> p[0] * p[0] + p[1] * p[1])
                .thenComparingInt(p -> p[0])
                .thenComparingInt(p -> p[1]);
        PriorityQueue<int[]> heap = new PriorityQueue<>(closer.reversed()); // farthest kept point on top
        for (int[] p : points) {
            heap.offer(p);
            if (heap.size() > k) heap.poll();
        }
        int[][] result = new int[heap.size()][];
        for (int i = result.length - 1; i >= 0; i--) result[i] = heap.poll();
        return result;
    }

    public static void main(String[] args) {
        check(Arrays.deepEquals(kClosest(new int[][] {{3, 4}, {1, -2}, {-2, 1}, {0, 5}, {2, 2}}, 3),
                new int[][] {{-2, 1}, {1, -2}, {2, 2}}));
        check(Arrays.deepEquals(kClosest(new int[][] {{2, 0}, {0, 2}, {0, -2}, {-2, 0}}, 2),
                new int[][] {{-2, 0}, {0, -2}}));
        check(kClosest(new int[][] {{5, 5}, {1, 1}}, 0).length == 0);
        check(Arrays.deepEquals(kClosest(new int[][] {{6, 1}, {-1, -1}, {0, 0}}, 3),
                new int[][] {{0, 0}, {-1, -1}, {6, 1}}));
        check(Arrays.deepEquals(kClosest(new int[][] {{1, 1}, {0, 3}, {1, 1}}, 2), new int[][] {{1, 1}, {1, 1}}));
        check(Arrays.deepEquals(kClosest(new int[][] {{10000, -10000}, {-10000, 10000}}, 1),
                new int[][] {{-10000, 10000}}));
        System.out.println("144 ok");
    }

    static void check(boolean ok) {
        if (!ok) throw new AssertionError("test failed");
    }
}
