// 049 · Push the Zeros to the End
import java.util.Arrays;

public class P049PushTheZerosToTheEnd {
    static void pushZerosToEnd(int[] slots) {
        int write = 0; // slots[0..write) holds the parcels seen so far, in order
        for (int read = 0; read < slots.length; read++) {
            if (slots[read] != 0) {
                int tmp = slots[write];
                slots[write] = slots[read];
                slots[read] = tmp;
                write++;
            }
        }
    }

    static int[] pushed(int[] slots) {
        pushZerosToEnd(slots);
        return slots;
    }

    public static void main(String[] args) {
        check(Arrays.equals(pushed(new int[] {0, 41, 0, 0, 17, 23}), new int[] {41, 17, 23, 0, 0, 0}));
        check(Arrays.equals(pushed(new int[] {5, 0, 3}), new int[] {5, 3, 0}));
        check(Arrays.equals(pushed(new int[0]), new int[0]));
        check(Arrays.equals(pushed(new int[] {0, 0}), new int[] {0, 0}));
        check(Arrays.equals(pushed(new int[] {1, 2}), new int[] {1, 2}));
        check(Arrays.equals(pushed(new int[] {0, 0, 1}), new int[] {1, 0, 0}));
        System.out.println("049 ok");
    }

    static void check(boolean ok) {
        if (!ok) throw new AssertionError("test failed");
    }
}
