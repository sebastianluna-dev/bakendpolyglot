// 086 · Remove the k-th Node From the End
import java.util.ArrayList;
import java.util.List;

public class P086RemoveTheKThNodeFromTheEnd {
    static class ListNode {
        int val;
        ListNode next;

        ListNode(int val, ListNode next) {
            this.val = val;
            this.next = next;
        }
    }

    static ListNode removeKthFromEnd(ListNode head, int k) {
        ListNode dummy = new ListNode(0, head); // the node before the head, in case the head goes
        ListNode lead = head;
        for (int i = 0; i < k; i++) lead = lead.next;
        ListNode trail = dummy;
        while (lead != null) {
            lead = lead.next;
            trail = trail.next;
        }
        trail.next = trail.next.next;
        return dummy.next;
    }

    static List<Integer> remove(int[] values, int k) {
        return toList(removeKthFromEnd(fromArray(values), k));
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
        check(remove(new int[] {10, 20, 30, 40, 50}, 2).equals(List.of(10, 20, 30, 50)));
        check(remove(new int[] {8}, 1).isEmpty());
        check(remove(new int[] {1, 2}, 2).equals(List.of(2)));
        check(remove(new int[] {1, 2}, 1).equals(List.of(1)));
        check(remove(new int[] {5, 6, 7}, 3).equals(List.of(6, 7)));
        check(remove(new int[] {5, 6, 7}, 1).equals(List.of(5, 6)));
        System.out.println("086 ok");
    }

    static void check(boolean ok) {
        if (!ok) throw new AssertionError("test failed");
    }
}
