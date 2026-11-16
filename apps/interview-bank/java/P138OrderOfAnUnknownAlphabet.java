// 138 · Order of an Unknown Alphabet
public class P138OrderOfAnUnknownAlphabet {
    static String alphabetOrder(String[] words) {
        boolean[] present = new boolean[26];
        int letters = 0;
        for (String w : words) {
            for (char ch : w.toCharArray()) {
                if (!present[ch - 'a']) letters++;
                present[ch - 'a'] = true;
            }
        }
        boolean[][] before = new boolean[26][26]; // before[x][y]: x comes before y
        int[] waiting = new int[26]; // letters that must come before this one and are not placed yet
        for (int i = 0; i + 1 < words.length; i++) {
            String a = words[i], b = words[i + 1];
            int j = 0;
            while (j < a.length() && j < b.length() && a.charAt(j) == b.charAt(j)) j++;
            if (j == a.length() || j == b.length()) {
                if (a.length() > b.length()) return ""; // a word sorted before its own prefix
                continue;
            }
            int x = a.charAt(j) - 'a', y = b.charAt(j) - 'a';
            if (!before[x][y]) {
                before[x][y] = true;
                waiting[y]++;
            }
        }
        boolean[] placed = new boolean[26];
        StringBuilder order = new StringBuilder();
        while (true) {
            int x = 0;
            while (x < 26 && !(present[x] && !placed[x] && waiting[x] == 0)) x++;
            if (x == 26) break;
            placed[x] = true;
            order.append((char) ('a' + x));
            for (int y = 0; y < 26; y++) {
                if (before[x][y]) waiting[y]--;
            }
        }
        return order.length() == letters ? order.toString() : ""; // shorter means a cycle
    }

    public static void main(String[] args) {
        check(alphabetOrder(new String[] {"cab", "cad", "ab", "ba", "bd"}).equals("cabd"));
        check(alphabetOrder(new String[] {"ba", "bc", "ad"}).equals("bacd"));
        check(alphabetOrder(new String[] {"abc", "ab"}).equals(""));
        check(alphabetOrder(new String[] {"a", "b", "a"}).equals(""));
        check(alphabetOrder(new String[] {"a", "b", "c", "a"}).equals(""));
        check(alphabetOrder(new String[] {"zyx"}).equals("xyz"));
        check(alphabetOrder(new String[] {"ab", "ab"}).equals("ab"));
        check(alphabetOrder(new String[] {"ab", "abc"}).equals("abc"));
        check(alphabetOrder(new String[] {"x", "z", "y"}).equals("xzy"));
        System.out.println("138 ok");
    }

    static void check(boolean ok) {
        if (!ok) throw new AssertionError("test failed");
    }
}
