// 150 · Merge k Sorted Lists
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.PriorityQueue;

public class P150MergeKSortedLists {
    static class ListNode {
        int val;
        ListNode next;

        ListNode(int val, ListNode next) {
            this.val = val;
            this.next = next;
        }
    }

    static ListNode mergeLists(ListNode[] lists) {
        // the front node of each list, by value
        PriorityQueue<ListNode> heap = new PriorityQueue<>(Comparator.comparingInt(n -> n.val));
        for (ListNode head : lists) {
            if (head != null) heap.offer(head);
        }
        ListNode dummy = new ListNode(0, null);
        ListNode tail = dummy;
        while (!heap.isEmpty()) {
            ListNode node = heap.poll();
            tail.next = node;
            tail = node;
            if (node.next != null) heap.offer(node.next);
        }
        return dummy.next;
    }

    static List<Integer> merge(int[][] lists) {
        ListNode[] heads = new ListNode[lists.length];
        for (int i = 0; i < lists.length; i++) heads[i] = fromArray(lists[i]);
        return toList(mergeLists(heads));
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
        check(merge(new int[][] {{1, 4, 7}, {2, 5}, {0, 6, 9}}).equals(List.of(0, 1, 2, 4, 5, 6, 7, 9)));
        check(merge(new int[][] {{}, {3}, {}}).equals(List.of(3)));
        check(merge(new int[0][]).isEmpty());
        check(merge(new int[][] {{}}).isEmpty());
        check(merge(new int[][] {{2, 2}, {2}, {1, 2}}).equals(List.of(1, 2, 2, 2, 2)));
        check(merge(new int[][] {{-5, 0, 5}}).equals(List.of(-5, 0, 5)));
        check(merge(new int[][] {{10}, {-10}, {0}, {-20, 20}}).equals(List.of(-20, -10, 0, 10, 20)));
        System.out.println("150 ok");
    }

    static void check(boolean ok) {
        if (!ok) throw new AssertionError("test failed");
    }
}
