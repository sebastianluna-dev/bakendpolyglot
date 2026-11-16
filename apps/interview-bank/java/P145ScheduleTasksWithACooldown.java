// 145 · Schedule Tasks With a Cooldown
import java.util.ArrayDeque;
import java.util.Comparator;
import java.util.PriorityQueue;

public class P145ScheduleTasksWithACooldown {
    static int scheduleTime(String tasks, int cooldown) {
        int[] counts = new int[26];
        for (char t : tasks.toCharArray()) counts[t - 'A']++;
        PriorityQueue<Integer> heap = new PriorityQueue<>(Comparator.reverseOrder()); // copies left, most first
        for (int c : counts) {
            if (c > 0) heap.offer(c);
        }
        ArrayDeque<int[]> waiting = new ArrayDeque<>(); // {copies left, time it may run again}
        int time = 0;
        while (!heap.isEmpty() || !waiting.isEmpty()) {
            time++;
            if (heap.isEmpty()) {
                time = waiting.peekFirst()[1]; // idle until the next type comes off cooldown
            } else {
                int left = heap.poll() - 1;
                if (left > 0) waiting.addLast(new int[] {left, time + cooldown});
            }
            if (!waiting.isEmpty() && waiting.peekFirst()[1] == time) heap.offer(waiting.pollFirst()[0]);
        }
        return time;
    }

    public static void main(String[] args) {
        check(scheduleTime("AAABBC", 2) == 7);
        check(scheduleTime("AABBCD", 1) == 6);
        check(scheduleTime("AAABBB", 2) == 8);
        check(scheduleTime("AAAA", 3) == 13);
        check(scheduleTime("ABAB", 0) == 4);
        check(scheduleTime("A", 5) == 1);
        check(scheduleTime("AAABCDEFG", 2) == 9);
        System.out.println("145 ok");
    }

    static void check(boolean ok) {
        if (!ok) throw new AssertionError("test failed");
    }
}
