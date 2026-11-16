// 001 · Duplicate Barcodes
import java.util.HashSet;
import java.util.Set;

public class P001DuplicateBarcodes {
    static boolean hasDuplicate(String[] barcodes) {
        Set<String> seen = new HashSet<>();
        for (String code : barcodes) {
            if (!seen.add(code)) return true; // add returns false when the code is already there
        }
        return false;
    }

    public static void main(String[] args) {
        check(hasDuplicate(new String[] {"SW-1042", "SW-0007", "SW-2210", "SW-0007"}));
        check(!hasDuplicate(new String[] {"SW-1042", "SW-0007", "SW-2210"}));
        check(!hasDuplicate(new String[] {"SW-0007", "sw-0007"}));
        check(!hasDuplicate(new String[] {"SW-1042"}));
        check(!hasDuplicate(new String[0]));
        System.out.println("001 ok");
    }

    static void check(boolean ok) {
        if (!ok) throw new AssertionError("test failed");
    }
}
