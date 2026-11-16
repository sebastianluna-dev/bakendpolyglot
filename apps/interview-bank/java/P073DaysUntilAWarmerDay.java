// 073 · Days Until a Warmer Day
import java.util.ArrayDeque;
import java.util.Arrays;
import java.util.Deque;

public class P073DaysUntilAWarmerDay {
    static int[] daysUntilWarmer(int[] temps) {
        int[] wait = new int[temps.length];
        Deque<Integer> stack = new ArrayDeque<>(); // indices still waiting, their temperatures never rising from bottom to top
        for (int i = 0; i < temps.length; i++) {
            while (!stack.isEmpty() && temps[stack.peek()] < temps[i]) {
                int j = stack.pop();
                wait[j] = i - j;
            }
            stack.push(i);
        }
        return wait;
    }

    public static void main(String[] args) {
        check(Arrays.equals(daysUntilWarmer(new int[] {18, 16, 17, 21, 20, 20, 23}), new int[] {3, 1, 1, 3, 2, 1, 0}));
        check(Arrays.equals(daysUntilWarmer(new int[] {30, 25, 20}), new int[] {0, 0, 0}));
        check(Arrays.equals(daysUntilWarmer(new int[0]), new int[0]));
        check(Arrays.equals(daysUntilWarmer(new int[] {12}), new int[] {0}));
        check(Arrays.equals(daysUntilWarmer(new int[] {5, 5, 5}), new int[] {0, 0, 0}));
        check(Arrays.equals(daysUntilWarmer(new int[] {-3, -1, 0, 2}), new int[] {1, 1, 1, 0}));
        System.out.println("073 ok");
    }

    static void check(boolean ok) {
        if (!ok) throw new AssertionError("test failed");
    }
}
