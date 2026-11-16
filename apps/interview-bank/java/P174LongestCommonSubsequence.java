// 174 · Longest Common Subsequence
public class P174LongestCommonSubsequence {
    static int longestCommonSubsequence(String a, String b) {
        int m = b.length();
        // cur[j]: answer for the first i letters of a and the first j of b; prev holds row i - 1
        int[] prev = new int[m + 1];
        for (int i = 1; i <= a.length(); i++) {
            int[] cur = new int[m + 1];
            for (int j = 1; j <= m; j++) {
                if (a.charAt(i - 1) == b.charAt(j - 1)) cur[j] = prev[j - 1] + 1;
                else cur[j] = Math.max(prev[j], cur[j - 1]);
            }
            prev = cur;
        }
        return prev[m];
    }

    public static void main(String[] args) {
        check(longestCommonSubsequence("parcel", "place") == 4);
        check(longestCommonSubsequence("seat", "lock") == 0);
        check(longestCommonSubsequence("", "abc") == 0);
        check(longestCommonSubsequence("abc", "") == 0);
        check(longestCommonSubsequence("member", "member") == 6);
        check(longestCommonSubsequence("aaaa", "aa") == 2);
        check(longestCommonSubsequence("delivery", "relived") == 5);
        System.out.println("174 ok");
    }

    static void check(boolean ok) {
        if (!ok) throw new AssertionError("test failed");
    }
}
