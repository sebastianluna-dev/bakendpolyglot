// 046 · Is One a Subsequence of the Other?
public class P046IsOneASubsequenceOfTheOther {
    static boolean isSubsequence(String plan, String scans) {
        int i = 0, j = 0;
        while (i < plan.length() && j < scans.length()) {
            if (plan.charAt(i) == scans.charAt(j)) i++;
            j++;
        }
        return i == plan.length();
    }

    public static void main(String[] args) {
        check(isSubsequence("ACE", "ABCDE"));
        check(!isSubsequence("AEC", "ABCDE"));
        check(isSubsequence("", "ABC"));
        check(!isSubsequence("A", ""));
        check(isSubsequence("AA", "ABA"));
        check(!isSubsequence("AAB", "ABAC"));
        System.out.println("046 ok");
    }

    static void check(boolean ok) {
        if (!ok) throw new AssertionError("test failed");
    }
}
