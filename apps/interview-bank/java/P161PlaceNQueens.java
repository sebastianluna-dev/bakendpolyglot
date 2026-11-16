// 161 · Place n Queens
import java.util.ArrayList;
import java.util.List;

public class P161PlaceNQueens {
    static List<List<String>> placeQueens(int n) {
        List<List<String>> result = new ArrayList<>();
        boolean[] cols = new boolean[n];
        boolean[] downs = new boolean[2 * n - 1]; // r + c is the same along a down-left diagonal
        boolean[] ups = new boolean[2 * n - 1]; // r - c + n - 1 is the same along a down-right diagonal
        walk(n, 0, new ArrayList<>(), cols, downs, ups, result);
        return result;
    }

    static void walk(int n, int r, List<Integer> queens, boolean[] cols, boolean[] downs, boolean[] ups, List<List<String>> result) {
        if (r == n) {
            List<String> board = new ArrayList<>();
            for (int c : queens) board.add(".".repeat(c) + "Q" + ".".repeat(n - c - 1));
            result.add(board);
            return;
        }
        for (int c = 0; c < n; c++) {
            if (cols[c] || downs[r + c] || ups[r - c + n - 1]) continue;
            cols[c] = downs[r + c] = ups[r - c + n - 1] = true;
            queens.add(c); // queens.get(r) is the column of the queen in row r
            walk(n, r + 1, queens, cols, downs, ups, result);
            queens.remove(queens.size() - 1);
            cols[c] = downs[r + c] = ups[r - c + n - 1] = false;
        }
    }

    public static void main(String[] args) {
        check(placeQueens(4).equals(List.of(List.of(".Q..", "...Q", "Q...", "..Q."), List.of("..Q.", "Q...", "...Q", ".Q.."))));
        check(placeQueens(1).equals(List.of(List.of("Q"))));
        check(placeQueens(2).isEmpty());
        check(placeQueens(3).isEmpty());
        check(placeQueens(6).size() == 4);
        List<List<String>> eight = placeQueens(8);
        check(eight.size() == 92);
        check(eight.get(0).equals(List.of("Q.......", "....Q...", ".......Q", ".....Q..", "..Q.....", "......Q.", ".Q......", "...Q....")));
        System.out.println("161 ok");
    }

    static void check(boolean ok) {
        if (!ok) throw new AssertionError("test failed");
    }
}
