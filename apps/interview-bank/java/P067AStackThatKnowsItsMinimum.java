// 067 · A Stack That Knows Its Minimum
import java.util.ArrayDeque;
import java.util.Deque;

public class P067AStackThatKnowsItsMinimum {
    static class MinStack {
        private final Deque<Integer> values = new ArrayDeque<>();
        private final Deque<Integer> mins = new ArrayDeque<>(); // each entry is the smallest value at or below it

        void push(int x) {
            values.push(x);
            mins.push(mins.isEmpty() ? x : Math.min(x, mins.peek()));
        }

        int pop() {
            mins.pop();
            return values.pop();
        }

        int top() {
            return values.peek();
        }

        int min() {
            return mins.peek();
        }
    }

    public static void main(String[] args) {
        MinStack s = new MinStack();
        s.push(5);
        s.push(3);
        s.push(7);
        check(s.min() == 3);
        check(s.top() == 7);
        check(s.pop() == 7);
        check(s.min() == 3);
        check(s.pop() == 3);
        check(s.min() == 5);
        check(s.pop() == 5);

        s.push(4);
        s.push(1);
        s.push(1);
        check(s.pop() == 1);
        check(s.min() == 1);
        check(s.pop() == 1);
        check(s.min() == 4);
        s.push(-2);
        check(s.min() == -2);
        check(s.top() == -2);
        check(s.pop() == -2);
        check(s.top() == 4);
        check(s.min() == 4);
        System.out.println("067 ok");
    }

    static void check(boolean ok) {
        if (!ok) throw new AssertionError("test failed");
    }
}
