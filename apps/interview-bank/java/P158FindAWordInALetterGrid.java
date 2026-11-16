// 158 · Find a Word in a Letter Grid
public class P158FindAWordInALetterGrid {
    static boolean findWord(String[] grid, String word) {
        boolean[][] used = new boolean[grid.length][grid[0].length()];
        for (int r = 0; r < grid.length; r++) {
            for (int c = 0; c < grid[0].length(); c++) {
                if (walk(grid, word, used, r, c, 0)) return true;
            }
        }
        return false;
    }

    static boolean walk(String[] grid, String word, boolean[][] used, int r, int c, int k) {
        if (k == word.length()) return true;
        if (r < 0 || r >= grid.length || c < 0 || c >= grid[0].length() || used[r][c] || grid[r].charAt(c) != word.charAt(k)) return false;
        used[r][c] = true;
        boolean found = walk(grid, word, used, r + 1, c, k + 1) || walk(grid, word, used, r - 1, c, k + 1)
                || walk(grid, word, used, r, c + 1, k + 1) || walk(grid, word, used, r, c - 1, k + 1);
        used[r][c] = false; // free the cell for paths that start elsewhere
        return found;
    }

    public static void main(String[] args) {
        String[] grid = {"she", "afl", "nao"};
        check(findWord(grid, "shelf"));
        check(!findWord(grid, "lol"));
        check(findWord(grid, "loan"));
        check(findWord(grid, "fan"));
        check(!findWord(grid, "sf"));
        check(findWord(grid, "o"));
        check(!findWord(grid, "z"));
        check(!findWord(grid, "shelfaloans"));
        check(findWord(new String[] {"aa", "ab"}, "aaab"));
        System.out.println("158 ok");
    }

    static void check(boolean ok) {
        if (!ok) throw new AssertionError("test failed");
    }
}
