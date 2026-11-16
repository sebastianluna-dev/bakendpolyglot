// 148 · How Far With Bricks and Ladders
import java.util.PriorityQueue;

public class P148HowFarWithBricksAndLadders {
    static int furthestPlatform(int[] heights, int bricks, int ladders) {
        PriorityQueue<Integer> onLadders = new PriorityQueue<>(); // the climbs ladders cover so far
        for (int i = 0; i < heights.length - 1; i++) {
            int climb = heights[i + 1] - heights[i];
            if (climb <= 0) continue;
            onLadders.offer(climb);
            if (onLadders.size() > ladders) bricks -= onLadders.poll(); // the smallest climb goes to bricks
            if (bricks < 0) return i;
        }
        return heights.length - 1;
    }

    public static void main(String[] args) {
        check(furthestPlatform(new int[] {3, 8, 6, 10, 11, 15, 7}, 4, 1) == 3);
        check(furthestPlatform(new int[] {10, 12, 13, 25, 26}, 4, 1) == 4);
        check(furthestPlatform(new int[] {7}, 0, 0) == 0);
        check(furthestPlatform(new int[] {9, 7, 7, 3}, 0, 0) == 3);
        check(furthestPlatform(new int[] {1, 2}, 0, 0) == 0);
        check(furthestPlatform(new int[] {1, 2, 3, 4}, 0, 3) == 3);
        check(furthestPlatform(new int[] {1, 6, 2, 9, 4, 5}, 13, 0) == 5);
        check(furthestPlatform(new int[] {1, 6, 2, 9, 4, 5}, 12, 0) == 4);
        System.out.println("148 ok");
    }

    static void check(boolean ok) {
        if (!ok) throw new AssertionError("test failed");
    }
}
