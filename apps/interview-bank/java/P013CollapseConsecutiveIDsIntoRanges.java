// 013 · Collapse Consecutive IDs Into Ranges
import java.util.ArrayList;
import java.util.List;

public class P013CollapseConsecutiveIDsIntoRanges {
    static List<String> collapseRanges(int[] ids) {
        List<String> ranges = new ArrayList<>();
        int i = 0;
        while (i < ids.length) {
            int start = ids[i];
            while (i + 1 < ids.length && ids[i + 1] == ids[i] + 1) i++;
            ranges.add(start == ids[i] ? String.valueOf(start) : start + "-" + ids[i]);
            i++;
        }
        return ranges;
    }

    public static void main(String[] args) {
        check(collapseRanges(new int[] {1, 2, 3, 7, 9, 10}).equals(List.of("1-3", "7", "9-10")));
        check(collapseRanges(new int[] {0, 2, 4}).equals(List.of("0", "2", "4")));
        check(collapseRanges(new int[0]).isEmpty());
        check(collapseRanges(new int[] {5}).equals(List.of("5")));
        check(collapseRanges(new int[] {40, 41, 42, 43}).equals(List.of("40-43")));
        System.out.println("013 ok");
    }

    static void check(boolean ok) {
        if (!ok) throw new AssertionError("test failed");
    }
}
