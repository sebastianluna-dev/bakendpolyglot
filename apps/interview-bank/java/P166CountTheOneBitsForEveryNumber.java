// 166 · Count the One Bits for Every Number
import java.util.Arrays;

public class P166CountTheOneBitsForEveryNumber {
    static int[] oneBitCounts(int n) {
        int[] bits = new int[n + 1];
        for (int i = 1; i <= n; i++) {
            bits[i] = bits[i >> 1] + (i & 1); // i without its last bit, plus that bit
        }
        return bits;
    }

    public static void main(String[] args) {
        check(Arrays.equals(oneBitCounts(5), new int[] {0, 1, 1, 2, 1, 2}));
        check(Arrays.equals(oneBitCounts(0), new int[] {0}));
        check(Arrays.equals(oneBitCounts(1), new int[] {0, 1}));
        check(Arrays.equals(oneBitCounts(8), new int[] {0, 1, 1, 2, 1, 2, 2, 3, 1}));
        check(oneBitCounts(1023)[1023] == 10);
        check(oneBitCounts(100000)[65535] == 16);
        System.out.println("166 ok");
    }

    static void check(boolean ok) {
        if (!ok) throw new AssertionError("test failed");
    }
}
