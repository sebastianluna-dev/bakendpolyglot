// 038 · Parse an Integer Strictly
import java.util.Objects;

public class P038ParseAnIntegerStrictly {
    static Integer parseInteger(String s) {
        int i = 0;
        boolean negative = false;
        if (!s.isEmpty() && (s.charAt(0) == '+' || s.charAt(0) == '-')) {
            negative = s.charAt(0) == '-';
            i = 1;
        }
        if (i == s.length()) return null; // empty, or a sign with no digits
        long limit = negative ? 2147483648L : Integer.MAX_VALUE;
        long value = 0;
        for (; i < s.length(); i++) {
            char ch = s.charAt(i);
            if (ch < '0' || ch > '9') return null;
            value = value * 10 + (ch - '0');
            if (value > limit) return null;
        }
        return (int) (negative ? -value : value);
    }

    public static void main(String[] args) {
        check(Objects.equals(parseInteger("42"), 42));
        check(Objects.equals(parseInteger("-17"), -17));
        check(Objects.equals(parseInteger("+8"), 8));
        check(Objects.equals(parseInteger("0042"), 42));
        check(Objects.equals(parseInteger("-0"), 0));
        check(Objects.equals(parseInteger("2147483647"), Integer.MAX_VALUE));
        check(Objects.equals(parseInteger("-2147483648"), Integer.MIN_VALUE));
        check(parseInteger("2147483648") == null);
        check(parseInteger("-2147483649") == null);
        check(parseInteger("99999999999999999999") == null);
        check(parseInteger("") == null);
        check(parseInteger("+") == null);
        check(parseInteger(" 42") == null);
        check(parseInteger("42 ") == null);
        check(parseInteger("12a") == null);
        check(parseInteger("+-3") == null);
        check(parseInteger("3.0") == null);
        System.out.println("038 ok");
    }

    static void check(boolean ok) {
        if (!ok) throw new AssertionError("test failed");
    }
}
