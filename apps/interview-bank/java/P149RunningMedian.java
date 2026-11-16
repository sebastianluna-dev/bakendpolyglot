// 149 · Running Median
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.PriorityQueue;

public class P149RunningMedian {
    static class RunningMedian {
        // the smaller half, largest on top
        private final PriorityQueue<Integer> low = new PriorityQueue<>(Comparator.reverseOrder());
        private final PriorityQueue<Integer> high = new PriorityQueue<>(); // the larger half, smallest on top

        void addNum(int num) {
            low.offer(num);
            high.offer(low.poll());
            if (high.size() > low.size()) low.offer(high.poll());
        }

        double findMedian() {
            if (low.size() > high.size()) return low.peek();
            return ((double) low.peek() + high.peek()) / 2; // double first: the int sum can overflow
        }
    }

    public static void main(String[] args) {
        RunningMedian m = new RunningMedian();
        m.addNum(5);
        check(m.findMedian() == 5.0);
        m.addNum(15);
        check(m.findMedian() == 10.0);
        m.addNum(1);
        check(m.findMedian() == 5.0);
        m.addNum(3);
        check(m.findMedian() == 4.0);

        RunningMedian neg = new RunningMedian();
        neg.addNum(-2);
        check(neg.findMedian() == -2.0);
        neg.addNum(-2);
        check(neg.findMedian() == -2.0);
        neg.addNum(7);
        check(neg.findMedian() == -2.0);
        neg.addNum(8);
        check(neg.findMedian() == 2.5);

        RunningMedian down = new RunningMedian();
        List<Double> medians = new ArrayList<>();
        for (int x : new int[] {9, 8, 7, 6, 5}) {
            down.addNum(x);
            medians.add(down.findMedian());
        }
        check(medians.equals(List.of(9.0, 8.5, 8.0, 7.5, 7.0)));

        RunningMedian big = new RunningMedian();
        big.addNum(Integer.MAX_VALUE);
        big.addNum(Integer.MAX_VALUE);
        check(big.findMedian() == Integer.MAX_VALUE);
        big.addNum(Integer.MIN_VALUE);
        big.addNum(Integer.MIN_VALUE);
        check(big.findMedian() == -0.5);
        System.out.println("149 ok");
    }

    static void check(boolean ok) {
        if (!ok) throw new AssertionError("test failed");
    }
}
