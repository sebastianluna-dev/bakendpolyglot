// 037 · Numbers to Roman Numerals
public class P037NumbersToRomanNumerals {
    static final int[] VALUES = {1000, 900, 500, 400, 100, 90, 50, 40, 10, 9, 5, 4, 1};
    static final String[] SYMBOLS = {"M", "CM", "D", "CD", "C", "XC", "L", "XL", "X", "IX", "V", "IV", "I"};

    static String toRoman(int n) {
        StringBuilder result = new StringBuilder();
        for (int i = 0; i < VALUES.length; i++) {
            while (n >= VALUES[i]) {
                result.append(SYMBOLS[i]);
                n -= VALUES[i];
            }
        }
        return result.toString();
    }

    public static void main(String[] args) {
        check(toRoman(2026).equals("MMXXVI"));
        check(toRoman(1994).equals("MCMXCIV"));
        check(toRoman(1).equals("I"));
        check(toRoman(4).equals("IV"));
        check(toRoman(49).equals("XLIX"));
        check(toRoman(58).equals("LVIII"));
        check(toRoman(444).equals("CDXLIV"));
        check(toRoman(3999).equals("MMMCMXCIX"));
        System.out.println("037 ok");
    }

    static void check(boolean ok) {
        if (!ok) throw new AssertionError("test failed");
    }
}
