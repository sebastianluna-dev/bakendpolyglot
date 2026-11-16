// 195 · LRU Cache
import java.util.HashMap;
import java.util.Map;

public class P195LruCache {
    static class Node {
        final int key;
        int value;
        Node prev = this, next = this;

        Node(int key, int value) {
            this.key = key;
            this.value = value;
        }
    }

    static class LruCache {
        private final int capacity;
        private final Map<Integer, Node> nodes = new HashMap<>();
        private final Node head = new Node(0, 0); // sentinel: head.next is the most recent, head.prev the least recent

        LruCache(int capacity) {
            this.capacity = capacity;
        }

        int get(int key) {
            Node node = nodes.get(key);
            if (node == null) return -1;
            unlink(node);
            pushFront(node);
            return node.value;
        }

        void put(int key, int value) {
            Node existing = nodes.get(key);
            if (existing != null) {
                existing.value = value;
                unlink(existing);
                pushFront(existing);
                return;
            }
            if (nodes.size() == capacity) {
                Node oldest = head.prev;
                unlink(oldest);
                nodes.remove(oldest.key);
            }
            Node node = new Node(key, value);
            nodes.put(key, node);
            pushFront(node);
        }

        private void unlink(Node node) {
            node.prev.next = node.next;
            node.next.prev = node.prev;
        }

        private void pushFront(Node node) {
            node.prev = head;
            node.next = head.next;
            head.next.prev = node;
            head.next = node;
        }
    }

    public static void main(String[] args) {
        LruCache c = new LruCache(2);
        c.put(1, 10);
        c.put(2, 20);
        check(c.get(1) == 10);
        c.put(3, 30); // evicts 2
        check(c.get(2) == -1);
        check(c.get(3) == 30);
        c.put(1, 11); // an update counts as a use
        c.put(4, 40); // evicts 3
        check(c.get(3) == -1);
        check(c.get(1) == 11);
        check(c.get(4) == 40);

        LruCache one = new LruCache(1);
        one.put(5, 50);
        check(one.get(5) == 50);
        one.put(6, 60);
        check(one.get(5) == -1);
        check(one.get(6) == 60);
        check(one.get(7) == -1);
        one.put(6, 61);
        check(one.get(6) == 61);
        System.out.println("195 ok");
    }

    static void check(boolean ok) {
        if (!ok) throw new AssertionError("test failed");
    }
}
