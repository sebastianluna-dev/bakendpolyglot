// 015 · The k Most Borrowed Books
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class P015TheKMostBorrowedBooks {
    static List<String> mostBorrowed(String[] loans, int k) {
        Map<String, Integer> counts = new HashMap<>();
        for (String title : loans) counts.merge(title, 1, Integer::sum);
        List<String> titles = new ArrayList<>(counts.keySet());
        titles.sort((a, b) -> {
            int ca = counts.get(a), cb = counts.get(b); // unbox: != on two Integers compares references
            return ca != cb ? Integer.compare(cb, ca) : a.compareTo(b);
        });
        return titles.subList(0, Math.min(k, titles.size()));
    }

    public static void main(String[] args) {
        String[] loans = {"dune", "emma", "dune", "ulysses", "emma", "dune", "beloved"};
        check(mostBorrowed(loans, 2).equals(List.of("dune", "emma")));
        check(mostBorrowed(new String[] {"emma", "beloved", "ulysses", "beloved", "emma"}, 2).equals(List.of("beloved", "emma")));
        check(mostBorrowed(new String[] {"dune"}, 3).equals(List.of("dune")));
        check(mostBorrowed(new String[0], 1).isEmpty());
        check(mostBorrowed(new String[] {"c", "b", "a"}, 2).equals(List.of("a", "b")));
        System.out.println("015 ok");
    }

    static void check(boolean ok) {
        if (!ok) throw new AssertionError("test failed");
    }
}
