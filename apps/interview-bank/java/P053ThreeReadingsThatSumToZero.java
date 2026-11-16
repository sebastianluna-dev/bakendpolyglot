// 053 · Three Readings That Sum to Zero
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class P053ThreeReadingsThatSumToZero {
    static List<List<Integer>> threeToZero(int[] readings) {
        int[] s = readings.clone();
        Arrays.sort(s);
        List<List<Integer>> result = new ArrayList<>();
        for (int i = 0; i < s.length - 2; i++) {
            if (i > 0 && s[i] == s[i - 1]) continue; // same first value, same triplets
            int lo = i + 1, hi = s.length - 1;
            while (lo < hi) {
                int sum = s[i] + s[lo] + s[hi];
                if (sum < 0) lo++;
                else if (sum > 0) hi--;
                else {
                    result.add(List.of(s[i], s[lo], s[hi]));
                    lo++;
                    hi--;
                    while (lo < hi && s[lo] == s[lo - 1]) lo++;
                }
            }
        }
        return result;
    }

    public static void main(String[] args) {
        check(threeToZero(new int[] {-4, 1, 3, 0, -1, 2, -1})
                .equals(List.of(List.of(-4, 1, 3), List.of(-1, -1, 2), List.of(-1, 0, 1))));
        check(threeToZero(new int[] {0, 0, 0, 0}).equals(List.of(List.of(0, 0, 0))));
        check(threeToZero(new int[] {1, 2, -4}).isEmpty());
        check(threeToZero(new int[0]).isEmpty());
        check(threeToZero(new int[] {5, -5}).isEmpty());
        check(threeToZero(new int[] {2, -2, 0, 2, 0}).equals(List.of(List.of(-2, 0, 2))));
        System.out.println("053 ok");
    }

    static void check(boolean ok) {
        if (!ok) throw new AssertionError("test failed");
    }
}
