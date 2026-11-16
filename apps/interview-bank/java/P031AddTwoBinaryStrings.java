// 031 · Add Two Binary Strings
public class P031AddTwoBinaryStrings {
    static String addBinary(String a, String b) {
        StringBuilder digits = new StringBuilder();
        int i = a.length() - 1, j = b.length() - 1, carry = 0;
        while (i >= 0 || j >= 0 || carry > 0) {
            int total = carry;
            if (i >= 0) total += a.charAt(i--) - '0';
            if (j >= 0) total += b.charAt(j--) - '0';
            digits.append(total % 2);
            carry = total / 2;
        }
        return digits.reverse().toString();
    }

    public static void main(String[] args) {
        check(addBinary("1011", "111").equals("10010"));
        check(addBinary("0", "0").equals("0"));
        check(addBinary("1", "1").equals("10"));
        check(addBinary("1111", "1").equals("10000"));
        check(addBinary("100", "110010").equals("110110"));
        check(addBinary("1".repeat(70), "1").equals("1" + "0".repeat(70)));
        System.out.println("031 ok");
    }

    static void check(boolean ok) {
        if (!ok) throw new AssertionError("test failed");
    }
}
