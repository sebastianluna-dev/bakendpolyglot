// 064 · Rain Caught Between Walls
public class P064RainCaughtBetweenWalls {
    static int trappedRain(int[] heights) {
        int left = 0, right = heights.length - 1, leftMax = 0, rightMax = 0, water = 0;
        while (left < right) {
            // The lower end decides: the far side has a wall at least this high.
            if (heights[left] < heights[right]) {
                leftMax = Math.max(leftMax, heights[left]);
                water += leftMax - heights[left];
                left++;
            } else {
                rightMax = Math.max(rightMax, heights[right]);
                water += rightMax - heights[right];
                right--;
            }
        }
        return water;
    }

    public static void main(String[] args) {
        check(trappedRain(new int[] {3, 0, 2, 0, 4}) == 7);
        check(trappedRain(new int[] {5, 4, 3}) == 0);
        check(trappedRain(new int[0]) == 0);
        check(trappedRain(new int[] {7}) == 0);
        check(trappedRain(new int[] {2, 0, 2}) == 2);
        check(trappedRain(new int[] {0, 2, 0, 1, 0, 3, 1, 0, 2}) == 8);
        check(trappedRain(new int[] {4, 1, 1, 4}) == 6);
        check(trappedRain(new int[] {3, 0, 1}) == 1);
        System.out.println("064 ok");
    }

    static void check(boolean ok) {
        if (!ok) throw new AssertionError("test failed");
    }
}
