// 085 · Drop Repeated Values From a Sorted List
import java.util.ArrayList;
import java.util.List;

public class P085DropRepeatedValuesFromASortedList {
    static class ListNode {
        int val;
        ListNode next;

        ListNode(int val, ListNode next) {
            this.val = val;
            this.next = next;
        }
    }

    static ListNode dropRepeats(ListNode head) {
        ListNode node = head;
        while (node != null && node.next != null) {
            if (node.next.val == node.val) node.next = node.next.next; // stay: the new next may repeat too
            else node = node.next;
        }
        return head;
    }

    static List<Integer> drop(int[] values) {
        return toList(dropRepeats(fromArray(values)));
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
        check(drop(new int[] {1, 1, 2, 3, 3, 3}).equals(List.of(1, 2, 3)));
        check(drop(new int[] {4, 4, 4}).equals(List.of(4)));
        check(drop(new int[0]).isEmpty());
        check(drop(new int[] {1, 2, 3}).equals(List.of(1, 2, 3)));
        check(drop(new int[] {-2, -2, 0, 5, 5}).equals(List.of(-2, 0, 5)));
        check(drop(new int[] {8}).equals(List.of(8)));
        System.out.println("085 ok");
    }

    static void check(boolean ok) {
        if (!ok) throw new AssertionError("test failed");
    }
}
