// 083 · Does the List Loop?
public class P083DoesTheListLoop {
    static class ListNode {
        int val;
        ListNode next;

        ListNode(int val, ListNode next) {
            this.val = val;
            this.next = next;
        }
    }

    static boolean hasLoop(ListNode head) {
        ListNode slow = head;
        ListNode fast = head;
        while (fast != null && fast.next != null) {
            slow = slow.next;
            fast = fast.next.next;
            if (slow == fast) return true;
        }
        return false;
    }

    // Builds the list and links its last node to the node at index loopTo (-1: no loop).
    static ListNode withLoop(int[] values, int loopTo) {
        ListNode[] nodes = new ListNode[values.length];
        for (int i = 0; i < values.length; i++) nodes[i] = new ListNode(values[i], null);
        for (int i = 0; i + 1 < nodes.length; i++) nodes[i].next = nodes[i + 1];
        if (loopTo >= 0) nodes[nodes.length - 1].next = nodes[loopTo];
        return nodes.length > 0 ? nodes[0] : null;
    }

    public static void main(String[] args) {
        check(hasLoop(withLoop(new int[] {3, 8, 1, 6}, 1)));
        check(!hasLoop(withLoop(new int[] {4, 2}, -1)));
        check(hasLoop(withLoop(new int[] {5}, 0)));
        check(!hasLoop(withLoop(new int[0], -1)));
        check(!hasLoop(withLoop(new int[] {2, 5, 2}, -1)));
        check(hasLoop(withLoop(new int[] {1, 2, 3, 4, 5, 6}, 0)));
        check(hasLoop(withLoop(new int[] {7, 1}, 1)));
        System.out.println("083 ok");
    }

    static void check(boolean ok) {
        if (!ok) throw new AssertionError("test failed");
    }
}
