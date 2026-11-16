// 199 · Bounded Worker Pool With Ordered Results
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Function;

public class P199BoundedWorkerPoolWithOrderedResults {
    static <T, R> List<R> mapLimit(List<T> items, int limit, Function<T, R> fn) throws Exception {
        List<R> results = new ArrayList<>(Collections.nCopies(items.size(), null));
        AtomicInteger next = new AtomicInteger();
        try (ExecutorService pool = Executors.newFixedThreadPool(limit)) {
            List<Future<?>> workers = new ArrayList<>();
            for (int w = 0; w < Math.min(limit, items.size()); w++)
                workers.add(pool.submit(() -> {
                    int i;
                    while ((i = next.getAndIncrement()) < items.size())
                        results.set(i, fn.apply(items.get(i))); // each index goes to one worker
                }));
            for (Future<?> f : workers) f.get(); // wait, and rethrow a worker's failure
        }
        return results;
    }

    record Run(List<Integer> results, int peak) {}

    static Run measure(List<Integer> durations, int limit) throws Exception {
        Object lock = new Object();
        int[] running = {0};
        int[] peak = {0};
        List<Integer> results = mapLimit(durations, limit, ms -> {
            synchronized (lock) {
                running[0]++;
                peak[0] = Math.max(peak[0], running[0]);
            }
            sleepMs(ms);
            synchronized (lock) {
                running[0]--;
            }
            return ms * 10;
        });
        return new Run(results, peak[0]); // f.get() in mapLimit made every task's writes visible
    }

    static void sleepMs(int ms) {
        try {
            Thread.sleep(ms);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    public static void main(String[] args) throws Exception {
        List<Integer> durations = List.of(40, 20, 30, 20, 25, 20, 30, 20); // later tasks often finish before earlier ones
        check(measure(durations, 3).equals(new Run(List.of(400, 200, 300, 200, 250, 200, 300, 200), 3)));
        check(measure(List.of(20, 20, 20), 1).equals(new Run(List.of(200, 200, 200), 1)));
        check(measure(List.of(30, 20), 10).equals(new Run(List.of(300, 200), 2)));
        check(measure(List.of(), 4).equals(new Run(List.of(), 0)));
        System.out.println("199 ok");
    }

    static void check(boolean ok) {
        if (!ok) throw new AssertionError("test failed");
    }
}
