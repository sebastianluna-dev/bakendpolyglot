// 056 · Longest Run After k Replacements
public class P056LongestRunAfterKReplacements {
    static int longestRunAfterReplacements(String s, int k) {
        int[] counts = new int[26];
        int left = 0, best = 0;
        for (int right = 0; right < s.length(); right++) {
            counts[s.charAt(right) - 'A']++;
            // Letters to replace = window length minus the most common letter in it.
            while (right - left + 1 - maxOf(counts) > k) {
                counts[s.charAt(left) - 'A']--;
                left++;
            }
            best = Math.max(best, right - left + 1);
        }
        return best;
    }

    static int maxOf(int[] counts) {
        int m = 0;
        for (int c : counts) m = Math.max(m, c);
        return m;
    }

    public static void main(String[] args) {
        check(longestRunAfterReplacements("ABAACBA", 1) == 4);
        check(longestRunAfterReplacements("XYXY", 2) == 4);
        check(longestRunAfterReplacements("", 0) == 0);
        check(longestRunAfterReplacements("AAAA", 0) == 4);
        check(longestRunAfterReplacements("ABCD", 0) == 1);
        check(longestRunAfterReplacements("AB", 5) == 2);
        check(longestRunAfterReplacements("BAAABBBBA", 2) == 6);
        System.out.println("056 ok");
    }

    static void check(boolean ok) {
        if (!ok) throw new AssertionError("test failed");
    }
}
