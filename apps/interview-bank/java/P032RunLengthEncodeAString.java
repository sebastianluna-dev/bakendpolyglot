// 032 · Run-Length Encode a String
public class P032RunLengthEncodeAString {
    static String runLengthEncode(String s) {
        StringBuilder result = new StringBuilder();
        int i = 0;
        while (i < s.length()) {
            int j = i;
            while (j < s.length() && s.charAt(j) == s.charAt(i)) j++;
            result.append(s.charAt(i)).append(j - i);
            i = j;
        }
        return result.toString();
    }

    public static void main(String[] args) {
        check(runLengthEncode("aaabccdddd").equals("a3b1c2d4"));
        check(runLengthEncode("abc").equals("a1b1c1"));
        check(runLengthEncode("").equals(""));
        check(runLengthEncode("z".repeat(12)).equals("z12"));
        check(runLengthEncode("aAaa").equals("a1A1a2"));
        check(runLengthEncode("abb").equals("a1b2"));
        System.out.println("032 ok");
    }

    static void check(boolean ok) {
        if (!ok) throw new AssertionError("test failed");
    }
}
