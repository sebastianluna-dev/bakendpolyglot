// 157 · Every Balanced Bracket String
import java.util.ArrayList;
import java.util.List;

public class P157EveryBalancedBracketString {
    static List<String> balancedBrackets(int n) {
        List<String> result = new ArrayList<>();
        walk(n, 0, 0, new StringBuilder(), result);
        return result;
    }

    static void walk(int n, int opened, int closed, StringBuilder path, List<String> result) {
        if (closed == n) {
            result.add(path.toString());
            return;
        }
        if (opened < n) {
            path.append('(');
            walk(n, opened + 1, closed, path, result);
            path.setLength(path.length() - 1);
        }
        if (closed < opened) {
            path.append(')');
            walk(n, opened, closed + 1, path, result);
            path.setLength(path.length() - 1);
        }
    }

    public static void main(String[] args) {
        check(balancedBrackets(3).equals(List.of("((()))", "(()())", "(())()", "()(())", "()()()")));
        check(balancedBrackets(1).equals(List.of("()")));
        check(balancedBrackets(0).equals(List.of("")));
        check(balancedBrackets(2).equals(List.of("(())", "()()")));
        check(balancedBrackets(4).size() == 14);
        System.out.println("157 ok");
    }

    static void check(boolean ok) {
        if (!ok) throw new AssertionError("test failed");
    }
}
