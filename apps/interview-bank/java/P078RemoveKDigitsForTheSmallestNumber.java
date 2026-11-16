// 078 · Remove k Digits for the Smallest Number
public class P078RemoveKDigitsForTheSmallestNumber {
    static String removeKDigits(String num, int k) {
        StringBuilder kept = new StringBuilder(); // used as a stack of digits
        for (char d : num.toCharArray()) {
            while (k > 0 && !kept.isEmpty() && kept.charAt(kept.length() - 1) > d) {
                kept.setLength(kept.length() - 1);
                k--;
            }
            kept.append(d);
        }
        kept.setLength(kept.length() - k); // digits never fell, so drop the rest from the end
        int start = 0;
        while (start < kept.length() - 1 && kept.charAt(start) == '0') start++;
        return kept.isEmpty() ? "0" : kept.substring(start);
    }

    public static void main(String[] args) {
        check(removeKDigits("4172", 2).equals("12"));
        check(removeKDigits("30200", 1).equals("200"));
        check(removeKDigits("9", 1).equals("0"));
        check(removeKDigits("12345", 2).equals("123"));
        check(removeKDigits("10", 1).equals("0"));
        check(removeKDigits("1432219", 0).equals("1432219"));
        check(removeKDigits("112", 1).equals("11"));
        check(removeKDigits("100200", 1).equals("200"));
        System.out.println("078 ok");
    }

    static void check(boolean ok) {
        if (!ok) throw new AssertionError("test failed");
    }
}
