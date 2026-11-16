// 066 · Balanced Brackets
import java.util.ArrayDeque;
import java.util.Deque;

public class P066BalancedBrackets {
    static boolean isBalanced(String s) {
        Deque<Character> expected = new ArrayDeque<>(); // the closer each open bracket is waiting for
        for (char ch : s.toCharArray()) {
            switch (ch) {
                case '(' -> expected.push(')');
                case '[' -> expected.push(']');
                case '{' -> expected.push('}');
                case ')', ']', '}' -> {
                    if (expected.isEmpty() || expected.pop() != ch) return false;
                }
            }
        }
        return expected.isEmpty();
    }

    public static void main(String[] args) {
        check(isBalanced("lookup(ids[0], {limit: 5})"));
        check(!isBalanced("([)]"));
        check(isBalanced(""));
        check(isBalanced("no brackets"));
        check(!isBalanced("("));
        check(!isBalanced(")"));
        check(!isBalanced("}{"));
        check(!isBalanced("(("));
        check(isBalanced("{[()()]}"));
        System.out.println("066 ok");
    }

    static void check(boolean ok) {
        if (!ok) throw new AssertionError("test failed");
    }
}
