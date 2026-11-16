// 029 · Roman Numerals to Numbers
import java.util.Map;

public class P029RomanNumeralsToNumbers {
    static final Map<Character, Integer> VALUES =
            Map.of('I', 1, 'V', 5, 'X', 10, 'L', 50, 'C', 100, 'D', 500, 'M', 1000);

    static int romanToNumber(String roman) {
        int total = 0;
        for (int i = 0; i < roman.length(); i++) {
            int value = VALUES.get(roman.charAt(i));
            int next = i + 1 < roman.length() ? VALUES.get(roman.charAt(i + 1)) : 0;
            // a smaller symbol before a larger one is subtracted (IV = 5 - 1)
            total += value < next ? -value : value;
        }
        return total;
    }

    public static void main(String[] args) {
        check(romanToNumber("MMXXVI") == 2026);
        check(romanToNumber("CDXLIV") == 444);
        check(romanToNumber("I") == 1);
        check(romanToNumber("IV") == 4);
        check(romanToNumber("IX") == 9);
        check(romanToNumber("XIV") == 14);
        check(romanToNumber("CM") == 900);
        check(romanToNumber("MMMCMXCIX") == 3999);
        System.out.println("029 ok");
    }

    static void check(boolean ok) {
        if (!ok) throw new AssertionError("test failed");
    }
}
