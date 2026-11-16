// 063 · Smallest Window Containing All Letters
public class P063SmallestWindowContainingAllLetters {
    static String smallestWindow(String text, String letters) {
        int[] need = new int[128]; // copies still wanted, by character code
        for (char c : letters.toCharArray()) need[c]++;
        int missing = letters.length(), bestStart = 0, bestLength = text.length() + 1, left = 0;
        for (int right = 0; right < text.length(); right++) {
            char c = text.charAt(right);
            if (need[c] > 0) missing--;
            need[c]--;
            while (missing == 0) {
                if (right - left + 1 < bestLength) {
                    bestStart = left;
                    bestLength = right - left + 1;
                }
                char d = text.charAt(left);
                need[d]++;
                if (need[d] > 0) missing++; // a wanted copy just left the window
                left++;
            }
        }
        return bestLength > text.length() ? "" : text.substring(bestStart, bestStart + bestLength);
    }

    public static void main(String[] args) {
        check(smallestWindow("deliverytruck", "tire").equals("iveryt"));
        check(smallestWindow("banana", "nn").equals("nan"));
        check(smallestWindow("abba", "ab").equals("ab"));
        check(smallestWindow("abc", "abd").equals(""));
        check(smallestWindow("a", "aa").equals(""));
        check(smallestWindow("Aa", "a").equals("a"));
        check(smallestWindow("", "a").equals(""));
        check(smallestWindow("parcel", "parcel").equals("parcel"));
        System.out.println("063 ok");
    }

    static void check(boolean ok) {
        if (!ok) throw new AssertionError("test failed");
    }
}
