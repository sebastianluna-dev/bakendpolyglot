// 087 · Interleave a List With Its Reverse
import java.util.ArrayList;
import java.util.List;

public class P087InterleaveAListWithItsReverse {
    static class ListNode {
        int val;
        ListNode next;

        ListNode(int val, ListNode next) {
            this.val = val;
            this.next = next;
        }
    }

    static void interleaveWithReverse(ListNode head) {
        if (head == null) return;
        ListNode slow = head;
        ListNode fast = head;
        while (fast.next != null && fast.next.next != null) {
            slow = slow.next;
            fast = fast.next.next;
        }
        ListNode second = reverse(slow.next);
        slow.next = null; // cut the first half off
        ListNode first = head;
        while (first != null && second != null) {
            ListNode nextFirst = first.next;
            ListNode nextSecond = second.next;
            first.next = second;
            second.next = nextFirst;
            first = nextFirst;
            second = nextSecond;
        }
    }

    static ListNode reverse(ListNode head) {
        ListNode prev = null;
        ListNode node = head;
        while (node != null) {
            ListNode next = node.next;
            node.next = prev;
            prev = node;
            node = next;
        }
        return prev;
    }

    static List<Integer> interleaved(int[] values) {
        ListNode head = fromArray(values);
        interleaveWithReverse(head);
        return toList(head);
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
        check(interleaved(new int[] {1, 2, 3, 4, 5, 6}).equals(List.of(1, 6, 2, 5, 3, 4)));
        check(interleaved(new int[] {3, 8, 5, 2, 9}).equals(List.of(3, 9, 8, 2, 5)));
        check(interleaved(new int[0]).isEmpty());
        check(interleaved(new int[] {7}).equals(List.of(7)));
        check(interleaved(new int[] {1, 2}).equals(List.of(1, 2)));
        check(interleaved(new int[] {10, 20, 30}).equals(List.of(10, 30, 20)));
        check(interleaved(new int[] {1, 2, 3, 4}).equals(List.of(1, 4, 2, 3)));
        System.out.println("087 ok");
    }

    static void check(boolean ok) {
        if (!ok) throw new AssertionError("test failed");
    }
}
