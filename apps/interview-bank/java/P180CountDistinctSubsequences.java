// 180 · Count Distinct Subsequences
public class P180CountDistinctSubsequences {
    static final int MOD = 1_000_000_007;

    static int countSubsequences(String s, String t) {
        int m = t.length();
        int[] ways = new int[m + 1]; // ways[j]: ways to spell the first j letters of t so far
        ways[0] = 1;
        for (char c : s.toCharArray()) {
            for (int j = m; j >= 1; j--) { // downwards, so this letter of s is used at most once
                if (t.charAt(j - 1) == c) ways[j] = (ways[j] + ways[j - 1]) % MOD;
            }
        }
        return ways[m];
    }

    public static void main(String[] args) {
        check(countSubsequences("banana", "ban") == 3);
        check(countSubsequences("seatseat", "sat") == 4);
        check(countSubsequences("abc", "abcd") == 0);
        check(countSubsequences("abc", "") == 1);
        check(countSubsequences("", "a") == 0);
        check(countSubsequences("", "") == 1);
        check(countSubsequences("aaaa", "aa") == 6);
        check(countSubsequences("a", "aa") == 0);
        check(countSubsequences("a".repeat(1000), "a".repeat(500)) == 159835829);
        System.out.println("180 ok");
    }

    static void check(boolean ok) {
        if (!ok) throw new AssertionError("test failed");
    }
}
