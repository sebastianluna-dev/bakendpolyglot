// 088 · Add Two Numbers Stored as Lists
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class P088AddTwoNumbersStoredAsLists {
    static class ListNode {
        int val;
        ListNode next;

        ListNode(int val, ListNode next) {
            this.val = val;
            this.next = next;
        }
    }

    static ListNode addLists(ListNode a, ListNode b) {
        ListNode dummy = new ListNode(0, null);
        ListNode tail = dummy;
        int carry = 0;
        while (a != null || b != null || carry > 0) {
            int sum = carry;
            if (a != null) {
                sum += a.val;
                a = a.next;
            }
            if (b != null) {
                sum += b.val;
                b = b.next;
            }
            carry = sum / 10;
            ListNode digit = new ListNode(sum % 10, null);
            tail.next = digit;
            tail = digit;
        }
        return dummy.next;
    }

    static List<Integer> add(int[] a, int[] b) {
        return toList(addLists(fromArray(a), fromArray(b)));
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
        check(add(new int[] {5, 2, 9}, new int[] {8, 4}).equals(List.of(3, 7, 9)));
        check(add(new int[] {9, 9, 9}, new int[] {1}).equals(List.of(0, 0, 0, 1)));
        check(add(new int[] {0}, new int[] {0}).equals(List.of(0)));
        check(add(new int[] {1}, new int[] {6, 0, 3}).equals(List.of(7, 0, 3)));
        check(add(new int[] {9, 9}, new int[] {9, 9}).equals(List.of(8, 9, 1)));
        int[] nines = new int[20];
        Arrays.fill(nines, 9);
        List<Integer> expected = new ArrayList<>(Collections.nCopies(20, 0));
        expected.add(1);
        check(add(nines, new int[] {1}).equals(expected));
        System.out.println("088 ok");
    }

    static void check(boolean ok) {
        if (!ok) throw new AssertionError("test failed");
    }
}
