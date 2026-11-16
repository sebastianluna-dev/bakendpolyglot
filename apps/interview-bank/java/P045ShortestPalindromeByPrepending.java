// 045 · Shortest Palindrome by Prepending
public class P045ShortestPalindromeByPrepending {
    static String shortestPalindrome(String s) {
        String reversed = new StringBuilder(s).reverse().toString();
        String t = s + "#" + reversed; // the '#' stops a match from running past the end of s
        int[] fail = new int[t.length()];
        int k = 0;
        for (int i = 1; i < t.length(); i++) {
            while (k > 0 && t.charAt(i) != t.charAt(k)) k = fail[k - 1];
            if (t.charAt(i) == t.charAt(k)) k++;
            fail[i] = k;
        }
        int keep = fail[t.length() - 1]; // length of the longest palindromic prefix of s
        return reversed.substring(0, s.length() - keep) + s;
    }

    public static void main(String[] args) {
        check(shortestPalindrome("abacd").equals("dcabacd"));
        check(shortestPalindrome("stats").equals("stats"));
        check(shortestPalindrome("").equals(""));
        check(shortestPalindrome("a").equals("a"));
        check(shortestPalindrome("ab").equals("bab"));
        check(shortestPalindrome("aabba").equals("abbaabba"));
        check(shortestPalindrome("aaa").equals("aaa"));
        System.out.println("045 ok");
    }

    static void check(boolean ok) {
        if (!ok) throw new AssertionError("test failed");
    }
}
