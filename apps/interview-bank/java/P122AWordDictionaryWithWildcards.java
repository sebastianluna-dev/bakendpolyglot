// 122 · A Word Dictionary With Wildcards
public class P122AWordDictionaryWithWildcards {
    static class TrieNode {
        TrieNode[] children = new TrieNode[26];
        boolean isWord;
    }

    static class WordDictionary {
        private final TrieNode root = new TrieNode();

        void addWord(String word) {
            TrieNode node = root;
            for (char ch : word.toCharArray()) {
                int i = ch - 'a';
                if (node.children[i] == null) node.children[i] = new TrieNode();
                node = node.children[i];
            }
            node.isWord = true;
        }

        boolean search(String pattern) {
            return match(root, pattern, 0);
        }

        private static boolean match(TrieNode node, String pattern, int i) {
            if (i == pattern.length()) return node.isWord;
            if (pattern.charAt(i) == '.') {
                for (TrieNode child : node.children) {
                    if (child != null && match(child, pattern, i + 1)) return true;
                }
                return false;
            }
            TrieNode next = node.children[pattern.charAt(i) - 'a'];
            return next != null && match(next, pattern, i + 1);
        }
    }

    public static void main(String[] args) {
        WordDictionary d = new WordDictionary();
        d.addWord("loan");
        d.addWord("lean");
        check(d.search("l.an"));
        check(!d.search("lo"));
        check(d.search("l..."));
        d.addWord("load");
        check(d.search("loan"));
        check(d.search("lo.d"));
        check(!d.search("le.d"));
        check(!d.search("loans"));
        check(d.search("...."));
        check(!d.search("..."));
        check(!d.search("x..."));

        WordDictionary e = new WordDictionary();
        check(!e.search("."));
        e.addWord("a");
        check(e.search("."));
        check(e.search("a"));
        check(!e.search("b"));
        e.addWord("a");
        check(e.search("a"));
        System.out.println("122 ok");
    }

    static void check(boolean ok) {
        if (!ok) throw new AssertionError("test failed");
    }
}
