// 048 · Remove Duplicates From a Sorted Array
import java.util.Arrays;

public class P048RemoveDuplicatesFromASortedArray {
    static int removeDuplicates(int[] ids) {
        if (ids.length == 0) return 0;
        int write = 1; // ids[0..write) holds the distinct ids kept so far
        for (int read = 1; read < ids.length; read++) {
            if (ids[read] != ids[write - 1]) ids[write++] = ids[read];
        }
        return write;
    }

    static int[] deduped(int[] ids) {
        int k = removeDuplicates(ids);
        return Arrays.copyOf(ids, k);
    }

    public static void main(String[] args) {
        check(Arrays.equals(deduped(new int[] {3, 3, 5, 8, 8, 8, 9}), new int[] {3, 5, 8, 9}));
        check(Arrays.equals(deduped(new int[] {7, 7, 7}), new int[] {7}));
        check(Arrays.equals(deduped(new int[0]), new int[0]));
        check(Arrays.equals(deduped(new int[] {1, 2, 3}), new int[] {1, 2, 3}));
        check(Arrays.equals(deduped(new int[] {10, 10, 20, 20, 30}), new int[] {10, 20, 30}));
        System.out.println("048 ok");
    }

    static void check(boolean ok) {
        if (!ok) throw new AssertionError("test failed");
    }
}
