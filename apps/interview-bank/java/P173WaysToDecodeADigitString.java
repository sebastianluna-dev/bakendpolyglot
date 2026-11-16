// 173 · Ways to Decode a Digit String
public class P173WaysToDecodeADigitString {
    static int countDecodings(String digits) {
        int twoBack = 0; // decodings of the first i - 2 digits
        int oneBack = 1; // decodings of the first i - 1 digits; the empty prefix has one
        for (int i = 1; i <= digits.length(); i++) {
            int ways = digits.charAt(i - 1) != '0' ? oneBack : 0;
            if (i >= 2) {
                int two = Integer.parseInt(digits.substring(i - 2, i));
                if (two >= 10 && two <= 26) ways += twoBack; // at least 10: "06" is not a letter
            }
            twoBack = oneBack;
            oneBack = ways;
        }
        return oneBack;
    }

    public static void main(String[] args) {
        check(countDecodings("2612") == 4);
        check(countDecodings("301") == 0);
        check(countDecodings("1201") == 1);
        check(countDecodings("106") == 1);
        check(countDecodings("0") == 0);
        check(countDecodings("100") == 0);
        check(countDecodings("27") == 1);
        check(countDecodings("11106") == 2);
        check(countDecodings("1".repeat(45)) == 1836311903);
        System.out.println("173 ok");
    }

    static void check(boolean ok) {
        if (!ok) throw new AssertionError("test failed");
    }
}
