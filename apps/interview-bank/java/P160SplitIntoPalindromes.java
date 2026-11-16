// 160 · Split Into Palindromes
import java.util.ArrayList;
import java.util.List;

public class P160SplitIntoPalindromes {
    static List<List<String>> palindromeSplits(String s) {
        List<List<String>> result = new ArrayList<>();
        walk(s, 0, new ArrayList<>(), result);
        return result;
    }

    static void walk(String s, int start, List<String> path, List<List<String>> result) {
        if (start == s.length()) {
            result.add(new ArrayList<>(path));
            return;
        }
        for (int end = start; end < s.length(); end++) {
            if (!isPalindrome(s, start, end)) continue;
            path.add(s.substring(start, end + 1));
            walk(s, end + 1, path, result);
            path.remove(path.size() - 1);
        }
    }

    static boolean isPalindrome(String s, int lo, int hi) {
        while (lo < hi) {
            if (s.charAt(lo) != s.charAt(hi)) return false;
            lo++;
            hi--;
        }
        return true;
    }

    public static void main(String[] args) {
        check(palindromeSplits("aab").equals(List.of(List.of("a", "a", "b"), List.of("aa", "b"))));
        check(palindromeSplits("noon").equals(List.of(List.of("n", "o", "o", "n"), List.of("n", "oo", "n"), List.of("noon"))));
        check(palindromeSplits("a").equals(List.of(List.of("a"))));
        check(palindromeSplits("ab").equals(List.of(List.of("a", "b"))));
        check(palindromeSplits("aaa").equals(List.of(List.of("a", "a", "a"), List.of("a", "aa"), List.of("aa", "a"), List.of("aaa"))));
        List<List<String>> all = palindromeSplits("racecar");
        check(all.get(all.size() - 1).equals(List.of("racecar")));
        System.out.println("160 ok");
    }

    static void check(boolean ok) {
        if (!ok) throw new AssertionError("test failed");
    }
}
