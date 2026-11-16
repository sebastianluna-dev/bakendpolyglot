// 159 · Phone Keypad Words
import java.util.ArrayList;
import java.util.List;

public class P159PhoneKeypadWords {
    static final String[] KEYS = {"", "", "abc", "def", "ghi", "jkl", "mno", "pqrs", "tuv", "wxyz"};

    static List<String> keypadWords(String digits) {
        List<String> result = new ArrayList<>();
        if (digits.isEmpty()) return result;
        walk(digits, 0, new StringBuilder(), result);
        return result;
    }

    static void walk(String digits, int k, StringBuilder path, List<String> result) {
        if (k == digits.length()) {
            result.add(path.toString());
            return;
        }
        for (char letter : KEYS[digits.charAt(k) - '0'].toCharArray()) {
            path.append(letter);
            walk(digits, k + 1, path, result);
            path.setLength(path.length() - 1);
        }
    }

    public static void main(String[] args) {
        check(keypadWords("23").equals(List.of("ad", "ae", "af", "bd", "be", "bf", "cd", "ce", "cf")));
        check(keypadWords("7").equals(List.of("p", "q", "r", "s")));
        check(keypadWords("").isEmpty());
        check(keypadWords("9").equals(List.of("w", "x", "y", "z")));
        List<String> words = keypadWords("79");
        check(words.size() == 16 && words.get(0).equals("pw") && words.get(15).equals("sz"));
        check(keypadWords("234").size() == 27);
        System.out.println("159 ok");
    }

    static void check(boolean ok) {
        if (!ok) throw new AssertionError("test failed");
    }
}
