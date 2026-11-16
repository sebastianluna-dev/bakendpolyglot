// 027 · First Letter That Appears Once

public class P027FirstLetterThatAppearsOnce {
    static int firstUniqueIndex(String word) {
        int[] counts = new int[26];
        for (char ch : word.toCharArray()) counts[ch - 'a']++;
        for (int i = 0; i < word.length(); i++) {
            if (counts[word.charAt(i) - 'a'] == 1) return i;
        }
        return -1;
    }

    public static void main(String[] args) {
        check(firstUniqueIndex("shelfwise") == 1);
        check(firstUniqueIndex("noon") == -1);
        check(firstUniqueIndex("") == -1);
        check(firstUniqueIndex("z") == 0);
        check(firstUniqueIndex("aabbc") == 4);
        check(firstUniqueIndex("pear") == 0);
        System.out.println("027 ok");
    }

    static void check(boolean ok) {
        if (!ok) throw new AssertionError("test failed");
    }
}
