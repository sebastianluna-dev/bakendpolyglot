// 003 · Same Letters, Different Order
public class P003SameLettersDifferentOrder {
    static boolean sameLetters(String a, String b) {
        if (a.length() != b.length()) return false;
        int[] counts = new int[26];
        for (int i = 0; i < a.length(); i++) {
            counts[a.charAt(i) - 'a']++;
            counts[b.charAt(i) - 'a']--;
        }
        for (int c : counts) {
            if (c != 0) return false;
        }
        return true;
    }

    public static void main(String[] args) {
        check(sameLetters("dusty", "study"));
        check(!sameLetters("aab", "abb"));
        check(!sameLetters("loan", "loans"));
        check(sameLetters("abc", "abc"));
        check(sameLetters("", ""));
        System.out.println("003 ok");
    }

    static void check(boolean ok) {
        if (!ok) throw new AssertionError("test failed");
    }
}
