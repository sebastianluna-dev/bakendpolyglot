// 070 · Remove Adjacent Pairs
public class P070RemoveAdjacentPairs {
    static String removePairs(String s) {
        StringBuilder kept = new StringBuilder(); // used as a stack: the result so far
        for (char ch : s.toCharArray()) {
            int n = kept.length();
            if (n > 0 && kept.charAt(n - 1) == ch) kept.setLength(n - 1);
            else kept.append(ch);
        }
        return kept.toString();
    }

    public static void main(String[] args) {
        check(removePairs("abccbd").equals("ad"));
        check(removePairs("aabb").equals(""));
        check(removePairs("").equals(""));
        check(removePairs("abc").equals("abc"));
        check(removePairs("aaa").equals("a"));
        check(removePairs("abba").equals(""));
        check(removePairs("mississippi").equals("m"));
        System.out.println("070 ok");
    }

    static void check(boolean ok) {
        if (!ok) throw new AssertionError("test failed");
    }
}
