// 057 · Contains a Scrambled Copy
import java.util.Arrays;

public class P057ContainsAScrambledCopy {
    static boolean containsScrambled(String pattern, String text) {
        int m = pattern.length();
        if (m > text.length()) return false;
        int[] need = new int[26];
        int[] have = new int[26];
        for (int i = 0; i < m; i++) {
            need[pattern.charAt(i) - 'a']++;
            have[text.charAt(i) - 'a']++;
        }
        if (Arrays.equals(need, have)) return true;
        for (int i = m; i < text.length(); i++) {
            have[text.charAt(i) - 'a']++; // letter entering the window
            have[text.charAt(i - m) - 'a']--; // letter leaving it
            if (Arrays.equals(need, have)) return true;
        }
        return false;
    }

    public static void main(String[] args) {
        check(containsScrambled("loan", "renewalnoted"));
        check(!containsScrambled("seat", "stage"));
        check(!containsScrambled("abc", "ab"));
        check(containsScrambled("ab", "ab"));
        check(containsScrambled("aab", "baxaba"));
        check(!containsScrambled("aab", "abbb"));
        check(containsScrambled("z", "abcz"));
        System.out.println("057 ok");
    }

    static void check(boolean ok) {
        if (!ok) throw new AssertionError("test failed");
    }
}
