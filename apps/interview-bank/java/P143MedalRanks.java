// 143 · Medal Ranks
import java.util.Arrays;
import java.util.PriorityQueue;

public class P143MedalRanks {
    static String[] medalRanks(int[] scores) {
        // indices, highest score on top
        PriorityQueue<Integer> heap = new PriorityQueue<>((a, b) -> Integer.compare(scores[b], scores[a]));
        for (int i = 0; i < scores.length; i++) heap.offer(i);
        String[] medals = {"Gold Medal", "Silver Medal", "Bronze Medal"};
        String[] ranks = new String[scores.length];
        for (int place = 1; !heap.isEmpty(); place++) {
            int i = heap.poll();
            ranks[i] = place <= 3 ? medals[place - 1] : String.valueOf(place);
        }
        return ranks;
    }

    public static void main(String[] args) {
        check(Arrays.equals(medalRanks(new int[] {88, 95, 70, 91}),
                new String[] {"Bronze Medal", "Gold Medal", "4", "Silver Medal"}));
        check(Arrays.equals(medalRanks(new int[] {3, 9, 1, 12, 5, 7}),
                new String[] {"5", "Silver Medal", "6", "Gold Medal", "4", "Bronze Medal"}));
        check(Arrays.equals(medalRanks(new int[] {42}), new String[] {"Gold Medal"}));
        check(Arrays.equals(medalRanks(new int[] {0, 5}), new String[] {"Silver Medal", "Gold Medal"}));
        check(medalRanks(new int[0]).length == 0);
        check(Arrays.equals(medalRanks(new int[] {10, 20, 30, 40, 50, 60, 70, 80, 90, 100, 110}),
                new String[] {"11", "10", "9", "8", "7", "6", "5", "4", "Bronze Medal", "Silver Medal", "Gold Medal"}));
        System.out.println("143 ok");
    }

    static void check(boolean ok) {
        if (!ok) throw new AssertionError("test failed");
    }
}
