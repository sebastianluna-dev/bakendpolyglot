// 010 · Number Triangle Rows
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class P010NumberTriangleRows {
    static List<List<Integer>> triangleRows(int n) {
        List<List<Integer>> rows = new ArrayList<>();
        for (int r = 0; r < n; r++) {
            List<Integer> row = new ArrayList<>(Collections.nCopies(r + 1, 1));
            for (int c = 1; c < r; c++) row.set(c, rows.get(r - 1).get(c - 1) + rows.get(r - 1).get(c));
            rows.add(row);
        }
        return rows;
    }

    public static void main(String[] args) {
        check(triangleRows(5).equals(List.of(
                List.of(1), List.of(1, 1), List.of(1, 2, 1), List.of(1, 3, 3, 1), List.of(1, 4, 6, 4, 1))));
        check(triangleRows(2).equals(List.of(List.of(1), List.of(1, 1))));
        check(triangleRows(1).equals(List.of(List.of(1))));
        check(triangleRows(0).isEmpty());
        check(triangleRows(30).get(29).get(14) == 77558760);
        System.out.println("010 ok");
    }

    static void check(boolean ok) {
        if (!ok) throw new AssertionError("test failed");
    }
}
