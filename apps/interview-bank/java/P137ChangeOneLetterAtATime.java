// 137 · Change One Letter at a Time
import java.util.ArrayDeque;
import java.util.Deque;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class P137ChangeOneLetterAtATime {
    static int shortestChain(String start, String target, String[] words) {
        Set<String> dictionary = new HashSet<>(List.of(words));
        Map<String, Integer> length = new HashMap<>(); // words in the shortest chain that ends here
        length.put(start, 1);
        Deque<String> queue = new ArrayDeque<>();
        queue.offer(start);
        while (!queue.isEmpty()) {
            String word = queue.poll();
            if (word.equals(target)) return length.get(word);
            char[] chars = word.toCharArray();
            for (int i = 0; i < chars.length; i++) {
                char original = chars[i];
                for (char c = 'a'; c <= 'z'; c++) {
                    chars[i] = c;
                    String next = new String(chars);
                    if (dictionary.contains(next) && !length.containsKey(next)) {
                        length.put(next, length.get(word) + 1);
                        queue.offer(next);
                    }
                }
                chars[i] = original;
            }
        }
        return 0;
    }

    public static void main(String[] args) {
        check(shortestChain("cold", "warm", new String[] {"cord", "card", "ward", "warm", "worm", "word", "wore"}) == 5);
        check(shortestChain("lead", "gold", new String[] {"load", "goad"}) == 0);
        check(shortestChain("cat", "cot", new String[] {"cot"}) == 2);
        check(shortestChain("cat", "dog", new String[0]) == 0);
        check(shortestChain("hit", "hot", new String[] {"hot", "hot", "hit"}) == 2);
        check(shortestChain("mine", "bond", new String[] {"mind", "bind", "bond", "mend"}) == 4);
        check(shortestChain("a", "c", new String[] {"b", "c"}) == 2);
        System.out.println("137 ok");
    }

    static void check(boolean ok) {
        if (!ok) throw new AssertionError("test failed");
    }
}
