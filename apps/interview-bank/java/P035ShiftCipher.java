// 035 · Shift Cipher
public class P035ShiftCipher {
    static String shiftCipher(String text, int shift) {
        int k = (shift % 26 + 26) % 26; // % keeps the sign of shift, so -1 must become 25
        char[] chars = text.toCharArray();
        for (int i = 0; i < chars.length; i++) {
            char ch = chars[i];
            if (ch >= 'a' && ch <= 'z') chars[i] = (char) ('a' + (ch - 'a' + k) % 26);
            else if (ch >= 'A' && ch <= 'Z') chars[i] = (char) ('A' + (ch - 'A' + k) % 26);
        }
        return new String(chars);
    }

    public static void main(String[] args) {
        check(shiftCipher("Hold seat 14!", 3).equals("Krog vhdw 14!"));
        check(shiftCipher("xyz", 2).equals("zab"));
        check(shiftCipher("Abc", -1).equals("Zab"));
        check(shiftCipher("abc", 52).equals("abc"));
        check(shiftCipher("Zz", -27).equals("Yy"));
        check(shiftCipher("Krog vhdw 14!", -3).equals("Hold seat 14!"));
        check(shiftCipher("", 5).equals(""));
        System.out.println("035 ok");
    }

    static void check(boolean ok) {
        if (!ok) throw new AssertionError("test failed");
    }
}
