// 028 · Longest Shared Prefix

public class P028LongestSharedPrefix {
    static String longestSharedPrefix(String[] words) {
        if (words.length == 0) return "";
        String first = words[0];
        for (int i = 0; i < first.length(); i++) {
            for (String word : words) {
                if (i == word.length() || word.charAt(i) != first.charAt(i)) return first.substring(0, i);
            }
        }
        return first;
    }

    public static void main(String[] args) {
        check(longestSharedPrefix(new String[] {"QA76.73", "QA76.76", "QA76.9"}).equals("QA76."));
        check(longestSharedPrefix(new String[] {"parcel", "pulse", "seat"}).equals(""));
        check(longestSharedPrefix(new String[0]).equals(""));
        check(longestSharedPrefix(new String[] {"solo"}).equals("solo"));
        check(longestSharedPrefix(new String[] {"ab", "a"}).equals("a"));
        check(longestSharedPrefix(new String[] {"", "x"}).equals(""));
        check(longestSharedPrefix(new String[] {"hold", "hold"}).equals("hold"));
        System.out.println("028 ok");
    }

    static void check(boolean ok) {
        if (!ok) throw new AssertionError("test failed");
    }
}
