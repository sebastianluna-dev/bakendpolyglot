// 146 · Rearrange So No Neighbours Match
import java.util.PriorityQueue;

public class P146RearrangeSoNoNeighboursMatch {
    static String rearrange(String s) {
        int[] counts = new int[26];
        for (char ch : s.toCharArray()) counts[ch - 'a']++;
        // {copies left, letter}: most copies first, then the earlier letter
        PriorityQueue<int[]> heap = new PriorityQueue<>((a, b) ->
                a[0] != b[0] ? Integer.compare(b[0], a[0]) : Integer.compare(a[1], b[1]));
        for (int i = 0; i < 26; i++) {
            if (counts[i] > 0) heap.offer(new int[] {counts[i], 'a' + i});
        }
        StringBuilder sb = new StringBuilder();
        int[] held = {0, 0}; // the letter just placed, kept out of the heap for one step
        while (!heap.isEmpty()) {
            int[] top = heap.poll();
            sb.append((char) top[1]);
            if (held[0] > 0) heap.offer(held);
            held = new int[] {top[0] - 1, top[1]};
        }
        return sb.length() == s.length() ? sb.toString() : "";
    }

    public static void main(String[] args) {
        check(rearrange("aab").equals("aba"));
        check(rearrange("aaab").equals(""));
        check(rearrange("vvvlo").equals("vlvov"));
        check(rearrange("aabbcc").equals("abcabc"));
        check(rearrange("aaabbc").equals("ababac"));
        check(rearrange("a").equals("a"));
        check(rearrange("zz").equals(""));
        System.out.println("146 ok");
    }

    static void check(boolean ok) {
        if (!ok) throw new AssertionError("test failed");
    }
}
