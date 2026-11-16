// 041 · Compare Version Strings
public class P041CompareVersionStrings {
    static int compareVersions(String a, String b) {
        int i = 0, j = 0;
        while (i < a.length() || j < b.length()) {
            int x = 0;
            while (i < a.length() && a.charAt(i) != '.') {
                x = x * 10 + (a.charAt(i) - '0');
                i++;
            }
            int y = 0;
            while (j < b.length() && b.charAt(j) != '.') {
                y = y * 10 + (b.charAt(j) - '0');
                j++;
            }
            if (x != y) return x < y ? -1 : 1;
            i++; // step over the dot; past the end, the next part reads as 0
            j++;
        }
        return 0;
    }

    public static void main(String[] args) {
        check(compareVersions("1.2.10", "1.2.9") == 1);
        check(compareVersions("3.0.0", "3") == 0);
        check(compareVersions("1.01", "1.001") == 0);
        check(compareVersions("0.9", "1.0") == -1);
        check(compareVersions("7.5.2.4", "7.5.3") == -1);
        check(compareVersions("1.0.1", "1") == 1);
        check(compareVersions("10.4", "9.12") == 1);
        System.out.println("041 ok");
    }

    static void check(boolean ok) {
        if (!ok) throw new AssertionError("test failed");
    }
}
