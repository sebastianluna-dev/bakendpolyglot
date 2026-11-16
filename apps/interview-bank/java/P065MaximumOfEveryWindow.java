// 065 · Maximum of Every Window
import java.util.ArrayDeque;
import java.util.Arrays;
import java.util.Deque;

public class P065MaximumOfEveryWindow {
    static int[] windowMaxima(int[] readings, int k) {
        int[] result = new int[Math.max(0, readings.length - k + 1)];
        Deque<Integer> window = new ArrayDeque<>(); // indices; their readings decrease from first to last
        for (int i = 0; i < readings.length; i++) {
            while (!window.isEmpty() && readings[window.peekLast()] <= readings[i]) window.pollLast();
            window.addLast(i);
            if (window.peekFirst() <= i - k) window.pollFirst();
            if (i >= k - 1) result[i - k + 1] = readings[window.peekFirst()];
        }
        return result;
    }

    public static void main(String[] args) {
        check(Arrays.equals(windowMaxima(new int[] {3, 1, -2, 5, 4, 2, 6}, 3), new int[] {3, 5, 5, 5, 6}));
        check(Arrays.equals(windowMaxima(new int[] {2, 7, 1}, 3), new int[] {7}));
        check(Arrays.equals(windowMaxima(new int[] {4, -1, 4}, 1), new int[] {4, -1, 4}));
        check(Arrays.equals(windowMaxima(new int[0], 2), new int[0]));
        check(Arrays.equals(windowMaxima(new int[] {1, 2}, 3), new int[0]));
        check(Arrays.equals(windowMaxima(new int[] {5, 5, 1, 5}, 2), new int[] {5, 5, 5}));
        check(Arrays.equals(windowMaxima(new int[] {9, 7, 5, 3}, 2), new int[] {9, 7, 5}));
        check(Arrays.equals(windowMaxima(new int[] {9, 1, 1, 1}, 2), new int[] {9, 1, 1}));
        System.out.println("065 ok");
    }

    static void check(boolean ok) {
        if (!ok) throw new AssertionError("test failed");
    }
}
