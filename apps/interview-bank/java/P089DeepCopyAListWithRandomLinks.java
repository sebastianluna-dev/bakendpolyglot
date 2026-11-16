// 089 · Deep-Copy a List With Random Links
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class P089DeepCopyAListWithRandomLinks {
    static class ListNode {
        int val;
        ListNode next;
        ListNode random;

        ListNode(int val, ListNode next) {
            this.val = val;
            this.next = next;
        }
    }

    static ListNode deepCopy(ListNode head) {
        Map<ListNode, ListNode> copyOf = new HashMap<>(); // original node -> its copy
        for (ListNode node = head; node != null; node = node.next) copyOf.put(node, new ListNode(node.val, null));
        for (ListNode node = head; node != null; node = node.next) {
            ListNode copy = copyOf.get(node);
            copy.next = copyOf.get(node.next); // HashMap.get(null) returns null here
            copy.random = copyOf.get(node.random);
        }
        return copyOf.get(head);
    }

    // Each pair is {value, index of the random target}, with -1 for no target.
    static ListNode fromPairs(int[][] pairs) {
        ListNode[] nodes = new ListNode[pairs.length];
        for (int i = 0; i < pairs.length; i++) nodes[i] = new ListNode(pairs[i][0], null);
        for (int i = 0; i < pairs.length; i++) {
            nodes[i].next = i + 1 < nodes.length ? nodes[i + 1] : null;
            if (pairs[i][1] >= 0) nodes[i].random = nodes[pairs[i][1]];
        }
        return nodes.length > 0 ? nodes[0] : null;
    }

    static int[][] toPairs(ListNode head) {
        Map<ListNode, Integer> index = new HashMap<>();
        int i = 0;
        for (ListNode node = head; node != null; node = node.next) index.put(node, i++);
        List<int[]> pairs = new ArrayList<>();
        for (ListNode node = head; node != null; node = node.next) {
            pairs.add(new int[] {node.val, node.random == null ? -1 : index.get(node.random)});
        }
        return pairs.toArray(new int[0][]);
    }

    static boolean sharesNode(ListNode a, ListNode b) {
        Set<ListNode> seen = new HashSet<>();
        for (ListNode node = a; node != null; node = node.next) seen.add(node);
        for (ListNode node = b; node != null; node = node.next) {
            if (seen.contains(node) || seen.contains(node.random)) return true;
        }
        return false;
    }

    static void checkCopy(int[][] pairs) {
        ListNode original = fromPairs(pairs);
        ListNode copy = deepCopy(original);
        check(Arrays.deepEquals(toPairs(copy), pairs));
        check(Arrays.deepEquals(toPairs(original), pairs));
        check(!sharesNode(original, copy));
    }

    public static void main(String[] args) {
        checkCopy(new int[][] {{4, 2}, {9, 0}, {6, -1}});
        checkCopy(new int[][] {{1, 1}, {2, 1}});
        checkCopy(new int[0][]);
        checkCopy(new int[][] {{5, 0}});
        checkCopy(new int[][] {{7, 1}, {7, 0}});
        checkCopy(new int[][] {{3, -1}, {8, -1}, {2, -1}});
        System.out.println("089 ok");
    }

    static void check(boolean ok) {
        if (!ok) throw new AssertionError("test failed");
    }
}
