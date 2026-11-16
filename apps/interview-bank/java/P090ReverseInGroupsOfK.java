// 090 · Reverse in Groups of k
import java.util.ArrayList;
import java.util.List;

public class P090ReverseInGroupsOfK {
    static class ListNode {
        int val;
        ListNode next;

        ListNode(int val, ListNode next) {
            this.val = val;
            this.next = next;
        }
    }

    static ListNode reverseInGroups(ListNode head, int k) {
        ListNode dummy = new ListNode(0, head);
        ListNode before = dummy; // the node just before the next group
        while (true) {
            ListNode last = before;
            for (int i = 0; i < k && last != null; i++) last = last.next;
            if (last == null) return dummy.next; // fewer than k nodes left: keep them as they are
            ListNode after = last.next;
            ListNode first = before.next;
            ListNode prev = after;
            ListNode node = first;
            while (node != after) {
                ListNode next = node.next;
                node.next = prev;
                prev = node;
                node = next;
            }
            before.next = last;
            before = first;
        }
    }

    static List<Integer> groups(int[] values, int k) {
        return toList(reverseInGroups(fromArray(values), k));
    }

    static ListNode fromArray(int[] values) {
        ListNode head = null;
        for (int i = values.length - 1; i >= 0; i--) head = new ListNode(values[i], head);
        return head;
    }

    static List<Integer> toList(ListNode head) {
        List<Integer> values = new ArrayList<>();
        for (ListNode node = head; node != null; node = node.next) values.add(node.val);
        return values;
    }

    public static void main(String[] args) {
        check(groups(new int[] {1, 2, 3, 4, 5, 6, 7, 8}, 3).equals(List.of(3, 2, 1, 6, 5, 4, 7, 8)));
        check(groups(new int[] {1, 2, 3, 4, 5, 6}, 2).equals(List.of(2, 1, 4, 3, 6, 5)));
        check(groups(new int[] {4, 5, 6}, 1).equals(List.of(4, 5, 6)));
        check(groups(new int[] {4, 5, 6}, 3).equals(List.of(6, 5, 4)));
        check(groups(new int[] {4, 5, 6}, 4).equals(List.of(4, 5, 6)));
        check(groups(new int[0], 2).isEmpty());
        ListNode head = fromArray(new int[] {1, 2, 3});
        check(reverseInGroups(head, 3).next.next == head);
        System.out.println("090 ok");
    }

    static void check(boolean ok) {
        if (!ok) throw new AssertionError("test failed");
    }
}
