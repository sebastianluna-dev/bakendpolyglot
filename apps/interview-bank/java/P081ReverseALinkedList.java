// 081 · Reverse a Linked List
import java.util.ArrayList;
import java.util.List;

public class P081ReverseALinkedList {
    static class ListNode {
        int val;
        ListNode next;

        ListNode(int val, ListNode next) {
            this.val = val;
            this.next = next;
        }
    }

    static ListNode reverseList(ListNode head) {
        ListNode prev = null;
        ListNode node = head;
        while (node != null) {
            ListNode next = node.next; // save it before the link is overwritten
            node.next = prev;
            prev = node;
            node = next;
        }
        return prev;
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
        check(toList(reverseList(fromArray(new int[] {4, 8, 15, 16, 23}))).equals(List.of(23, 16, 15, 8, 4)));
        check(toList(reverseList(fromArray(new int[] {2, 9}))).equals(List.of(9, 2)));
        check(toList(reverseList(fromArray(new int[] {7}))).equals(List.of(7)));
        check(reverseList(fromArray(new int[0])) == null);
        check(toList(reverseList(fromArray(new int[] {5, 5, 1}))).equals(List.of(1, 5, 5)));
        System.out.println("081 ok");
    }

    static void check(boolean ok) {
        if (!ok) throw new AssertionError("test failed");
    }
}
