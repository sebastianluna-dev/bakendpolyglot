// 077 · A Fixed-Size Ring Buffer
public class P077AFixedSizeRingBuffer {
    static class RingBuffer {
        private final int[] items;
        private int head; // index of the oldest value
        private int count;

        RingBuffer(int capacity) {
            items = new int[capacity];
        }

        boolean enqueue(int value) {
            if (isFull()) return false;
            items[(head + count) % items.length] = value;
            count++;
            return true;
        }

        int dequeue() {
            if (isEmpty()) return -1;
            int value = items[head];
            head = (head + 1) % items.length;
            count--;
            return value;
        }

        int front() {
            return isEmpty() ? -1 : items[head];
        }

        int rear() {
            return isEmpty() ? -1 : items[(head + count - 1) % items.length];
        }

        boolean isEmpty() {
            return count == 0;
        }

        boolean isFull() {
            return count == items.length;
        }
    }

    public static void main(String[] args) {
        RingBuffer b = new RingBuffer(3);
        check(b.isEmpty());
        check(b.enqueue(5));
        check(b.enqueue(8));
        check(b.enqueue(2));
        check(!b.enqueue(9));
        check(b.isFull());
        check(b.rear() == 2);
        check(b.dequeue() == 5);
        check(b.enqueue(9));
        check(b.front() == 8);
        check(b.rear() == 9);
        check(b.dequeue() == 8);
        check(b.dequeue() == 2);
        check(b.dequeue() == 9);
        check(b.dequeue() == -1);
        check(b.isEmpty());
        check(b.front() == -1);
        check(b.rear() == -1);

        RingBuffer one = new RingBuffer(1);
        check(one.enqueue(0));
        check(one.isFull());
        check(!one.enqueue(1));
        check(one.front() == 0);
        check(one.rear() == 0);
        check(one.dequeue() == 0);
        check(one.isEmpty());
        System.out.println("077 ok");
    }

    static void check(boolean ok) {
        if (!ok) throw new AssertionError("test failed");
    }
}
