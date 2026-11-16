// 178 · Pattern Matching With . and *
public class P178PatternMatchingWithAnd {
    static boolean matchesPattern(String text, String pattern) {
        int m = pattern.length();
        // cur[j]: the first i letters of text match the first j characters of pattern; prev holds row i - 1
        boolean[] prev = new boolean[m + 1];
        for (int i = 0; i <= text.length(); i++) {
            boolean[] cur = new boolean[m + 1];
            cur[0] = i == 0;
            for (int j = 1; j <= m; j++) {
                if (pattern.charAt(j - 1) == '*') {
                    // the starred item matches nothing, or it takes text[i - 1] and may take more
                    cur[j] = cur[j - 2] || (i > 0 && fits(text.charAt(i - 1), pattern.charAt(j - 2)) && prev[j]);
                } else {
                    cur[j] = i > 0 && fits(text.charAt(i - 1), pattern.charAt(j - 1)) && prev[j - 1];
                }
            }
            prev = cur;
        }
        return prev[m];
    }

    static boolean fits(char c, char p) {
        return p == '.' || p == c;
    }

    public static void main(String[] args) {
        check(matchesPattern("seat", "se.*t"));
        check(!matchesPattern("seats", "se.*t"));
        check(matchesPattern("", "a*b*"));
        check(matchesPattern("", ""));
        check(!matchesPattern("a", ""));
        check(!matchesPattern("", "."));
        check(matchesPattern("xxy", "z*x*y"));
        check(!matchesPattern("ac", "a.c"));
        check(matchesPattern("aaa", "a*a"));
        check(matchesPattern("bbb", "bc*b*d*b"));
        check(matchesPattern("b", "a*b"));
        check(matchesPattern("ab", ".*"));
        check(!matchesPattern("abcd", "d*"));
        check(!matchesPattern("aaaaaaaaaaaaaaaaaaab", "a*a*a*a*a*a*a*a*c"));
        System.out.println("178 ok");
    }

    static void check(boolean ok) {
        if (!ok) throw new AssertionError("test failed");
    }
}
