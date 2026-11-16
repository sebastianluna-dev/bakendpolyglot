// 102 · Split Parcels Across k Trucks With the Lightest Peak
import java.util.Arrays;

public class P102SplitParcelsAcrossKTrucksWithTheLightestPeak {
    static int lightestPeak(int[] weights, int k) {
        int lo = Arrays.stream(weights).max().getAsInt();
        int hi = Arrays.stream(weights).sum();
        while (lo < hi) {
            int mid = lo + (hi - lo) / 2;
            if (trucksNeeded(weights, mid) <= k) hi = mid;
            else lo = mid + 1;
        }
        return lo;
    }

    static int trucksNeeded(int[] weights, int limit) {
        int trucks = 1, load = 0;
        for (int w : weights) {
            if (load + w > limit) {
                trucks++;
                load = 0;
            }
            load += w;
        }
        return trucks;
    }

    public static void main(String[] args) {
        check(lightestPeak(new int[] {4, 9, 3, 6, 8, 2}, 3) == 13);
        check(lightestPeak(new int[] {3, 1, 12, 2}, 3) == 12);
        check(lightestPeak(new int[] {4, 9, 3}, 1) == 16);
        check(lightestPeak(new int[] {2, 8, 5}, 3) == 8);
        check(lightestPeak(new int[] {7}, 1) == 7);
        check(lightestPeak(new int[] {1, 1, 1, 1, 1, 1}, 4) == 2);
        System.out.println("102 ok");
    }

    static void check(boolean ok) {
        if (!ok) throw new AssertionError("test failed");
    }
}
