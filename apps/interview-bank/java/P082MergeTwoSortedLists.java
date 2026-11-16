// 082 · Merge Two Sorted Lists
import java.util.ArrayList;
import java.util.List;

public class P082MergeTwoSortedLists {
    static class ListNode {
        int val;
        ListNode next;

        ListNode(int val, ListNode next) {
            this.val = val;
            this.next = next;
        }
    }

    static ListNode mergeSorted(ListNode a, ListNode b) {
        ListNode dummy = new ListNode(0, null);
        ListNode tail = dummy;
        while (a != null && b != null) {
            if (a.val <= b.val) {
                tail.next = a;
                tail = a;
                a = a.next;
            } else {
                tail.next = b;
                tail = b;
                b = b.next;
            }
        }
        tail.next = a != null ? a : b; // whatever is left is already sorted
        return dummy.next;
    }

    static List<Integer> merge(int[] a, int[] b) {
        return toList(mergeSorted(fromArray(a), fromArray(b)));
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
        check(merge(new int[] {1, 4, 6}, new int[] {2, 3, 6, 9}).equals(List.of(1, 2, 3, 4, 6, 6, 9)));
        check(merge(new int[0], new int[] {5, 8}).equals(List.of(5, 8)));
        check(merge(new int[0], new int[0]).isEmpty());
        check(merge(new int[] {7, 8}, new int[] {1, 2}).equals(List.of(1, 2, 7, 8)));
        check(merge(new int[] {-3, 0}, new int[] {-3}).equals(List.of(-3, -3, 0)));
        ListNode first = fromArray(new int[] {2});
        ListNode second = fromArray(new int[] {2});
        ListNode merged = mergeSorted(first, second);
        check(merged == first && merged.next == second);
        System.out.println("082 ok");
    }

    static void check(boolean ok) {
        if (!ok) throw new AssertionError("test failed");
    }
}
