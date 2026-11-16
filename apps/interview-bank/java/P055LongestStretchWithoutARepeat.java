// 055 · Longest Stretch Without a Repeat
import java.util.HashMap;
import java.util.Map;

public class P055LongestStretchWithoutARepeat {
    static int longestWithoutRepeat(String s) {
        Map<Character, Integer> last = new HashMap<>(); // character -> index where it was last seen
        int start = 0, best = 0;
        for (int i = 0; i < s.length(); i++) {
            Integer prev = last.get(s.charAt(i));
            if (prev != null && prev >= start) start = prev + 1; // only a repeat inside the window counts
            last.put(s.charAt(i), i);
            best = Math.max(best, i - start + 1);
        }
        return best;
    }

    public static void main(String[] args) {
        check(longestWithoutRepeat("shelfwise") == 7);
        check(longestWithoutRepeat("aaaa") == 1);
        check(longestWithoutRepeat("") == 0);
        check(longestWithoutRepeat("abba") == 2);
        check(longestWithoutRepeat("dvdf") == 3);
        check(longestWithoutRepeat("A-1 a-2") == 5);
        System.out.println("055 ok");
    }

    static void check(boolean ok) {
        if (!ok) throw new AssertionError("test failed");
    }
}
