// 039 · Longest Palindromic Stretch
public class P039LongestPalindromicStretch {
    static String longestPalindrome(String s) {
        int bestStart = 0, bestLength = 0;
        for (int i = 0; i < s.length(); i++) {
            for (int length : new int[] {expand(s, i, i), expand(s, i, i + 1)}) {
                if (length > bestLength) { // strictly longer, so the first stretch wins a tie
                    bestLength = length;
                    bestStart = i - (length - 1) / 2;
                }
            }
        }
        return s.substring(bestStart, bestStart + bestLength);
    }

    static int expand(String s, int lo, int hi) {
        while (lo >= 0 && hi < s.length() && s.charAt(lo) == s.charAt(hi)) {
            lo--;
            hi++;
        }
        return hi - lo - 1;
    }

    public static void main(String[] args) {
        check(longestPalindrome("xabbaracecarz").equals("racecar"));
        check(longestPalindrome("abcde").equals("a"));
        check(longestPalindrome("xyzzyabba").equals("yzzy"));
        check(longestPalindrome("banana").equals("anana"));
        check(longestPalindrome("abacab").equals("bacab"));
        check(longestPalindrome("aaaa").equals("aaaa"));
        check(longestPalindrome("a").equals("a"));
        check(longestPalindrome("").equals(""));
        System.out.println("039 ok");
    }

    static void check(boolean ok) {
        if (!ok) throw new AssertionError("test failed");
    }
}
