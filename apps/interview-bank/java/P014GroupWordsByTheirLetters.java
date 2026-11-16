// 014 · Group Words by Their Letters
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class P014GroupWordsByTheirLetters {
    static List<List<String>> groupByLetters(String[] words) {
        Map<String, Integer> groupOf = new HashMap<>(); // sorted letters -> index in groups
        List<List<String>> groups = new ArrayList<>();
        for (String word : words) {
            char[] letters = word.toCharArray();
            Arrays.sort(letters);
            String key = new String(letters);
            Integer g = groupOf.get(key);
            if (g == null) {
                g = groups.size();
                groupOf.put(key, g);
                groups.add(new ArrayList<>());
            }
            groups.get(g).add(word);
        }
        return groups;
    }

    public static void main(String[] args) {
        check(groupByLetters(new String[] {"listen", "stone", "silent", "notes", "enlist", "tones", "tab"})
                .equals(List.of(List.of("listen", "silent", "enlist"), List.of("stone", "notes", "tones"), List.of("tab"))));
        check(groupByLetters(new String[] {"loop", "pool", "polo", "lop"})
                .equals(List.of(List.of("loop", "pool", "polo"), List.of("lop"))));
        check(groupByLetters(new String[0]).isEmpty());
        check(groupByLetters(new String[] {"ab", "ba", "ab"}).equals(List.of(List.of("ab", "ba", "ab"))));
        check(groupByLetters(new String[] {"", "a", ""}).equals(List.of(List.of("", ""), List.of("a"))));
        System.out.println("014 ok");
    }

    static void check(boolean ok) {
        if (!ok) throw new AssertionError("test failed");
    }
}
