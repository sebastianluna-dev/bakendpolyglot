// 076 · Colliding Parcels on a Conveyor
import java.util.ArrayDeque;
import java.util.Arrays;
import java.util.Deque;

public class P076CollidingParcelsOnAConveyor {
    static int[] survivingParcels(int[] parcels) {
        Deque<Integer> track = new ArrayDeque<>(); // pushed and popped at the back, so it reads left to right
        for (int p : parcels) {
            boolean alive = true;
            while (alive && p < 0 && !track.isEmpty() && track.peekLast() > 0) {
                int top = track.peekLast();
                if (top < -p) {
                    track.pollLast();
                } else if (top == -p) {
                    track.pollLast();
                    alive = false;
                } else {
                    alive = false;
                }
            }
            if (alive) track.offerLast(p);
        }
        return track.stream().mapToInt(Integer::intValue).toArray();
    }

    public static void main(String[] args) {
        check(Arrays.equals(survivingParcels(new int[] {4, 7, -5}), new int[] {4, 7}));
        check(Arrays.equals(survivingParcels(new int[] {3, 6, -6, -8, 2}), new int[] {-8, 2}));
        check(Arrays.equals(survivingParcels(new int[0]), new int[0]));
        check(Arrays.equals(survivingParcels(new int[] {-3, 4}), new int[] {-3, 4}));
        check(Arrays.equals(survivingParcels(new int[] {5, -5}), new int[0]));
        check(Arrays.equals(survivingParcels(new int[] {1, -2, -2, -2}), new int[] {-2, -2, -2}));
        check(Arrays.equals(survivingParcels(new int[] {10, 2, -5}), new int[] {10}));
        System.out.println("076 ok");
    }

    static void check(boolean ok) {
        if (!ok) throw new AssertionError("test failed");
    }
}
