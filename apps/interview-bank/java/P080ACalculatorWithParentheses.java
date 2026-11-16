// 080 · A Calculator With Parentheses
import java.util.ArrayDeque;
import java.util.Deque;

public class P080ACalculatorWithParentheses {
    static int calculate(String s) {
        int result = 0, sign = 1, number = 0;
        Deque<Integer> stack = new ArrayDeque<>(); // for each open bracket: the result before it, then the sign in front of it
        for (char ch : s.toCharArray()) {
            if (ch >= '0' && ch <= '9') {
                number = number * 10 + (ch - '0');
            } else if (ch == '+' || ch == '-') {
                result += sign * number;
                number = 0;
                sign = ch == '+' ? 1 : -1;
            } else if (ch == '(') {
                stack.push(result);
                stack.push(sign);
                result = 0;
                sign = 1;
            } else if (ch == ')') {
                result += sign * number;
                number = 0;
                int outerSign = stack.pop();
                result = stack.pop() + outerSign * result;
            }
        }
        return result + sign * number;
    }

    public static void main(String[] args) {
        check(calculate("7 - (3 - (1 + 2))") == 7);
        check(calculate("-(4 - (-2 + 9)) + 10") == 13);
        check(calculate("12") == 12);
        check(calculate(" 1 + 2 ") == 3);
        check(calculate("-5") == -5);
        check(calculate("(((3)))") == 3);
        check(calculate("10 - 2 - 3") == 5);
        check(calculate("1 - (2 + 3)") == -4);
        check(calculate("-(-(-1))") == -1);
        check(calculate("2147483647") == 2147483647);
        System.out.println("080 ok");
    }

    static void check(boolean ok) {
        if (!ok) throw new AssertionError("test failed");
    }
}
