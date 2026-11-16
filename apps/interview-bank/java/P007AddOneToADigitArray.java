// 007 · Add One to a Digit Array
import java.util.Arrays;

public class P007AddOneToADigitArray {
    static int[] addOne(int[] digits) {
        int[] result = digits.clone();
        for (int i = result.length - 1; i >= 0; i--) {
            if (result[i] < 9) {
                result[i]++;
                return result;
            }
            result[i] = 0;
        }
        int[] longer = new int[result.length + 1]; // every digit was 9; Java zero-fills
        longer[0] = 1;
        return longer;
    }

    public static void main(String[] args) {
        int[] input = {1, 2, 9};
        int[] nines = new int[20];
        Arrays.fill(nines, 9);
        int[] power = new int[21];
        power[0] = 1;

        check(Arrays.equals(addOne(input), new int[] {1, 3, 0}));
        check(Arrays.equals(input, new int[] {1, 2, 9}));
        check(Arrays.equals(addOne(new int[] {9, 9, 9}), new int[] {1, 0, 0, 0}));
        check(Arrays.equals(addOne(new int[] {4, 5}), new int[] {4, 6}));
        check(Arrays.equals(addOne(new int[] {0}), new int[] {1}));
        check(Arrays.equals(addOne(nines), power));
        System.out.println("007 ok");
    }

    static void check(boolean ok) {
        if (!ok) throw new AssertionError("test failed");
    }
}
