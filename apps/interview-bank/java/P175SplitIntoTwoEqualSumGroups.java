// 175 · Split Into Two Equal-Sum Groups
import java.util.Arrays;

public class P175SplitIntoTwoEqualSumGroups {
    static boolean canSplitEvenly(int[] weights) {
        int total = Arrays.stream(weights).sum();
        if (total % 2 != 0) return false;
        int half = total / 2;
        boolean[] reachable = new boolean[half + 1]; // reachable[s]: some parcels seen so far weigh s
        reachable[0] = true;
        for (int w : weights) {
            for (int s = half; s >= w; s--) { // downwards, so this parcel is counted at most once
                if (reachable[s - w]) reachable[s] = true;
            }
        }
        return reachable[half];
    }

    public static void main(String[] args) {
        int[] many = new int[200];
        Arrays.fill(many, 100);
        check(canSplitEvenly(new int[] {3, 1, 4, 2, 2}));
        check(!canSplitEvenly(new int[] {2, 3, 7}));
        check(!canSplitEvenly(new int[] {1}));
        check(canSplitEvenly(new int[] {5, 5}));
        check(!canSplitEvenly(new int[] {1, 5}));
        check(!canSplitEvenly(new int[] {1, 2, 4}));
        check(canSplitEvenly(new int[] {1, 2, 3, 4, 5, 6, 7}));
        check(!canSplitEvenly(new int[] {6, 6, 6, 6, 6, 4}));
        check(canSplitEvenly(many));
        System.out.println("175 ok");
    }

    static void check(boolean ok) {
        if (!ok) throw new AssertionError("test failed");
    }
}
