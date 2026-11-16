// 030 · Letter-for-Letter Mapping
import java.util.HashMap;
import java.util.Map;

public class P030LetterForLetterMapping {
    static boolean canMapLetters(String source, String target) {
        if (source.length() != target.length()) return false;
        Map<Character, Character> forward = new HashMap<>();
        Map<Character, Character> backward = new HashMap<>(); // stops two letters mapping to the same one
        for (int i = 0; i < source.length(); i++) {
            char a = source.charAt(i), b = target.charAt(i);
            if (forward.getOrDefault(a, b) != b || backward.getOrDefault(b, a) != a) return false;
            forward.put(a, b);
            backward.put(b, a);
        }
        return true;
    }

    public static void main(String[] args) {
        check(canMapLetters("deed", "noon"));
        check(!canMapLetters("bell", "cool"));
        check(canMapLetters("", ""));
        check(!canMapLetters("a", "bc"));
        check(canMapLetters("abc", "bca"));
        check(!canMapLetters("ab", "aa"));
        check(!canMapLetters("aa", "ab"));
        check(!canMapLetters("tab", "tat"));
        System.out.println("030 ok");
    }

    static void check(boolean ok) {
        if (!ok) throw new AssertionError("test failed");
    }
}
