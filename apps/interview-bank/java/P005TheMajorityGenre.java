// 005 · The Majority Genre
import java.util.HashMap;
import java.util.Map;

public class P005TheMajorityGenre {
    static String majorityGenre(String[] loans) {
        Map<String, Integer> counts = new HashMap<>();
        for (String genre : loans) {
            int c = counts.merge(genre, 1, Integer::sum); // merge returns the new count
            if (c * 2 > loans.length) return genre;
        }
        return "";
    }

    public static void main(String[] args) {
        check(majorityGenre(new String[] {"mystery", "poetry", "mystery", "history", "mystery"}).equals("mystery"));
        check(majorityGenre(new String[] {"poetry", "history", "poetry", "history"}).equals(""));
        check(majorityGenre(new String[] {"drama", "comedy", "drama"}).equals("drama"));
        check(majorityGenre(new String[] {"sci-fi"}).equals("sci-fi"));
        check(majorityGenre(new String[0]).equals(""));
        System.out.println("005 ok");
    }

    static void check(boolean ok) {
        if (!ok) throw new AssertionError("test failed");
    }
}
