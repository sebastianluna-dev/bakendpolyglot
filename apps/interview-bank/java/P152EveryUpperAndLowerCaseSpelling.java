// 152 · Every Upper- and Lower-Case Spelling
import java.util.ArrayList;
import java.util.List;

public class P152EveryUpperAndLowerCaseSpelling {
    static List<String> allSpellings(String code) {
        List<String> out = new ArrayList<>();
        spell(code.toCharArray(), 0, out);
        return out;
    }

    static void spell(char[] chars, int i, List<String> out) {
        if (i == chars.length) {
            out.add(new String(chars));
            return;
        }
        char c = chars[i];
        if (!Character.isLetter(c)) { // a digit: one choice
            spell(chars, i + 1, out);
            return;
        }
        chars[i] = Character.toUpperCase(c); // upper case first keeps the output sorted
        spell(chars, i + 1, out);
        chars[i] = Character.toLowerCase(c);
        spell(chars, i + 1, out);
    }

    public static void main(String[] args) {
        check(allSpellings("b7x").equals(List.of("B7X", "B7x", "b7X", "b7x")));
        check(allSpellings("42").equals(List.of("42")));
        check(allSpellings("Q").equals(List.of("Q", "q")));
        check(allSpellings("aB1").equals(List.of("AB1", "Ab1", "aB1", "ab1")));
        check(allSpellings("0z0").equals(List.of("0Z0", "0z0")));
        check(allSpellings("abcdefghij").size() == 1024);
        System.out.println("152 ok");
    }

    static void check(boolean ok) {
        if (!ok) throw new AssertionError("test failed");
    }
}
