// 084 · The Middle Node
import java.util.ArrayList;
import java.util.List;

public class P084TheMiddleNode {
    static class ListNode {
        int val;
        ListNode next;

        ListNode(int val, ListNode next) {
            this.val = val;
            this.next = next;
        }
    }

    static ListNode middleNode(ListNode head) {
        ListNode slow = head;
        ListNode fast = head;
        while (fast != null && fast.next != null) {
            slow = slow.next;
            fast = fast.next.next;
        }
        return slow;
    }

    static List<Integer> fromMiddle(int[] values) {
        return toList(middleNode(fromArray(values)));
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
        check(fromMiddle(new int[] {3, 1, 4, 1, 5}).equals(List.of(4, 1, 5)));
        check(fromMiddle(new int[] {2, 7, 1, 8}).equals(List.of(1, 8)));
        check(fromMiddle(new int[] {6}).equals(List.of(6)));
        check(middleNode(null) == null);
        check(fromMiddle(new int[] {9, 4}).equals(List.of(4)));
        ListNode head = fromArray(new int[] {10, 20, 30});
        check(middleNode(head) == head.next);
        System.out.println("084 ok");
    }

    static void check(boolean ok) {
        if (!ok) throw new AssertionError("test failed");
    }
}
