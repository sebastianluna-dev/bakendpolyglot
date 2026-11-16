// 072 · Evaluate Postfix Expressions
import java.util.ArrayDeque;
import java.util.Deque;

public class P072EvaluatePostfixExpressions {
    static int evaluatePostfix(String[] tokens) {
        Deque<Integer> stack = new ArrayDeque<>();
        for (String token : tokens) {
            switch (token) {
                case "+", "-", "*", "/" -> {
                    int b = stack.pop();
                    int a = stack.pop();
                    stack.push(switch (token) {
                        case "+" -> a + b;
                        case "-" -> a - b;
                        case "*" -> a * b;
                        default -> a / b;
                    });
                }
                default -> stack.push(Integer.parseInt(token));
            }
        }
        return stack.peek();
    }

    public static void main(String[] args) {
        check(evaluatePostfix(new String[] {"4", "13", "5", "/", "+"}) == 6);
        check(evaluatePostfix(new String[] {"-7", "2", "/", "3", "*"}) == -9);
        check(evaluatePostfix(new String[] {"42"}) == 42);
        check(evaluatePostfix(new String[] {"8", "2", "/", "3", "-"}) == 1);
        check(evaluatePostfix(new String[] {"2", "-5", "/"}) == 0);
        check(evaluatePostfix(new String[] {"-9", "-2", "/"}) == 4);
        check(evaluatePostfix(new String[] {"5", "1", "2", "+", "4", "*", "+", "3", "-"}) == 14);
        System.out.println("072 ok");
    }

    static void check(boolean ok) {
        if (!ok) throw new AssertionError("test failed");
    }
}
