// 172 · Can the Text Be Split Into Words?
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class P172CanTheTextBeSplitIntoWords {
    static boolean canSplit(String text, List<String> words) {
        Set<String> dictionary = new HashSet<>(words);
        int longest = 0;
        for (String w : words) longest = Math.max(longest, w.length());
        boolean[] splits = new boolean[text.length() + 1]; // splits[i]: the first i letters split into words
        splits[0] = true;
        for (int end = 1; end <= text.length(); end++) {
            for (int len = 1; len <= Math.min(end, longest); len++) {
                if (splits[end - len] && dictionary.contains(text.substring(end - len, end))) {
                    splits[end] = true;
                    break;
                }
            }
        }
        return splits[text.length()];
    }

    public static void main(String[] args) {
        check(canSplit("bookseller", List.of("books", "book", "seller")));
        check(!canSplit("trucking", List.of("truck", "king", "in")));
        check(canSplit("", List.of("a")));
        check(!canSplit("abcd", List.of()));
        check(canSplit("seatseat", List.of("seat")));
        check(canSplit("aaaaaaa", List.of("aaaa", "aaa")));
        check(!canSplit("aaaaaaaaab", List.of("a", "aa")));
        System.out.println("172 ok");
    }

    static void check(boolean ok) {
        if (!ok) throw new AssertionError("test failed");
    }
}
