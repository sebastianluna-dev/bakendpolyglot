// 074 · Expand Repeated Patterns
import java.util.ArrayDeque;
import java.util.Deque;

public class P074ExpandRepeatedPatterns {
    static String expandPatterns(String s) {
        Deque<Integer> counts = new ArrayDeque<>();
        Deque<StringBuilder> outer = new ArrayDeque<>(); // the text built before each open bracket
        StringBuilder current = new StringBuilder();
        int count = 0;
        for (char ch : s.toCharArray()) {
            if (ch >= '0' && ch <= '9') {
                count = count * 10 + (ch - '0');
            } else if (ch == '[') {
                counts.push(count);
                outer.push(current);
                current = new StringBuilder();
                count = 0;
            } else if (ch == ']') {
                String inner = current.toString();
                current = outer.pop().append(inner.repeat(counts.pop()));
            } else {
                current.append(ch);
            }
        }
        return current.toString();
    }

    public static void main(String[] args) {
        check(expandPatterns("3[ab]2[c]").equals("abababcc"));
        check(expandPatterns("x2[y3[z]]").equals("xyzzzyzzz"));
        check(expandPatterns("").equals(""));
        check(expandPatterns("plain").equals("plain"));
        check(expandPatterns("12[q]").equals("qqqqqqqqqqqq"));
        check(expandPatterns("1[a1[b1[c]]]d").equals("abcd"));
        check(expandPatterns("2[a2[b]c]").equals("abbcabbc"));
        System.out.println("074 ok");
    }

    static void check(boolean ok) {
        if (!ok) throw new AssertionError("test failed");
    }
}
