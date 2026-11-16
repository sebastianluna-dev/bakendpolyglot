// 042 · Multiply Numbers Written as Strings
public class P042MultiplyNumbersWrittenAsStrings {
    static String multiplyStrings(String a, String b) {
        int[] digits = new int[a.length() + b.length()];
        for (int i = a.length() - 1; i >= 0; i--) {
            for (int j = b.length() - 1; j >= 0; j--) {
                int sum = (a.charAt(i) - '0') * (b.charAt(j) - '0') + digits[i + j + 1];
                digits[i + j + 1] = sum % 10;
                digits[i + j] += sum / 10; // may pass 9 for now; fixed when its own turn comes
            }
        }
        int start = 0;
        while (start < digits.length - 1 && digits[start] == 0) start++;
        StringBuilder result = new StringBuilder();
        for (int k = start; k < digits.length; k++) result.append(digits[k]);
        return result.toString();
    }

    public static void main(String[] args) {
        check(multiplyStrings("123", "45").equals("5535"));
        check(multiplyStrings("0", "9081").equals("0"));
        check(multiplyStrings("999", "999").equals("998001"));
        check(multiplyStrings("7", "8").equals("56"));
        check(multiplyStrings("1", "1").equals("1"));
        check(multiplyStrings("123456789", "987654321").equals("121932631112635269"));
        check(multiplyStrings("99999999999999999999", "99999999999999999999")
                .equals("9999999999999999999800000000000000000001"));
        System.out.println("042 ok");
    }

    static void check(boolean ok) {
        if (!ok) throw new AssertionError("test failed");
    }
}
