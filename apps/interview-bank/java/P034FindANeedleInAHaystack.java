// 034 · Find a Needle in a Haystack
public class P034FindANeedleInAHaystack {
    static int findNeedle(String haystack, String needle) {
        for (int start = 0; start + needle.length() <= haystack.length(); start++) {
            int k = 0;
            while (k < needle.length() && haystack.charAt(start + k) == needle.charAt(k)) k++;
            if (k == needle.length()) return start;
        }
        return -1;
    }

    public static void main(String[] args) {
        check(findNeedle("hold-14-seat-14", "14") == 5);
        check(findNeedle("parcel", "pulse") == -1);
        check(findNeedle("abc", "c") == 2);
        check(findNeedle("aaab", "ab") == 2);
        check(findNeedle("abc", "") == 0);
        check(findNeedle("", "") == 0);
        check(findNeedle("", "a") == -1);
        check(findNeedle("ab", "abc") == -1);
        System.out.println("034 ok");
    }

    static void check(boolean ok) {
        if (!ok) throw new AssertionError("test failed");
    }
}
