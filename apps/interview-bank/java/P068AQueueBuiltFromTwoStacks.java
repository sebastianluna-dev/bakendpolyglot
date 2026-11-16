// 068 · A Queue Built From Two Stacks
import java.util.ArrayDeque;
import java.util.Deque;

public class P068AQueueBuiltFromTwoStacks {
    static class TwoStackQueue {
        private final Deque<Integer> inbox = new ArrayDeque<>(); // newest on top
        private final Deque<Integer> outbox = new ArrayDeque<>(); // oldest on top

        void enqueue(int x) {
            inbox.push(x);
        }

        int dequeue() {
            refill();
            return outbox.pop();
        }

        int peek() {
            refill();
            return outbox.peek();
        }

        boolean isEmpty() {
            return inbox.isEmpty() && outbox.isEmpty();
        }

        // Only when outbox is empty, or newer items would jump ahead of older ones.
        private void refill() {
            if (!outbox.isEmpty()) return;
            while (!inbox.isEmpty()) outbox.push(inbox.pop());
        }
    }

    public static void main(String[] args) {
        TwoStackQueue q = new TwoStackQueue();
        check(q.isEmpty());
        q.enqueue(1);
        q.enqueue(2);
        check(q.peek() == 1);
        check(q.dequeue() == 1);
        q.enqueue(3);
        check(q.dequeue() == 2);
        check(q.peek() == 3);
        check(!q.isEmpty());
        check(q.dequeue() == 3);
        check(q.isEmpty());
        q.enqueue(4);
        q.enqueue(5);
        check(q.dequeue() == 4);
        q.enqueue(6);
        check(q.dequeue() == 5);
        check(q.dequeue() == 6);
        check(q.isEmpty());
        System.out.println("068 ok");
    }

    static void check(boolean ok) {
        if (!ok) throw new AssertionError("test failed");
    }
}
