// 177 · Edit Distance
public class P177EditDistance {
    static int editDistance(String source, String target) {
        int m = target.length();
        // cur[j]: fewest edits turning the first i letters of source into the first j of target; prev holds row i - 1
        int[] prev = new int[m + 1];
        for (int j = 0; j <= m; j++) prev[j] = j;
        for (int i = 1; i <= source.length(); i++) {
            int[] cur = new int[m + 1];
            cur[0] = i;
            for (int j = 1; j <= m; j++) {
                if (source.charAt(i - 1) == target.charAt(j - 1)) cur[j] = prev[j - 1];
                else cur[j] = 1 + Math.min(prev[j], Math.min(cur[j - 1], prev[j - 1])); // delete, insert, replace
            }
            prev = cur;
        }
        return prev[m];
    }

    public static void main(String[] args) {
        check(editDistance("shelf", "shelves") == 3);
        check(editDistance("loan", "lone") == 2);
        check(editDistance("", "abc") == 3);
        check(editDistance("abc", "") == 3);
        check(editDistance("seat", "seat") == 0);
        check(editDistance("parcel", "pencil") == 3);
        check(editDistance("trucks", "truck") == 1);
        check(editDistance("abc", "cab") == 2);
        check(editDistance("booking", "looking") == 1);
        System.out.println("177 ok");
    }

    static void check(boolean ok) {
        if (!ok) throw new AssertionError("test failed");
    }
}
