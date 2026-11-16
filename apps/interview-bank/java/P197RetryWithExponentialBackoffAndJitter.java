// 197 · Retry With Exponential Backoff and Jitter
import java.util.ArrayList;
import java.util.List;
import java.util.function.DoubleSupplier;
import java.util.function.IntConsumer;
import java.util.function.Supplier;

public class P197RetryWithExponentialBackoffAndJitter {
    static <T> T retry(Supplier<T> operation, int maxAttempts, int baseDelayMs, int capMs,
                       IntConsumer sleep, DoubleSupplier random) {
        int ceiling = baseDelayMs;
        for (int attempt = 1; attempt < maxAttempts; attempt++) {
            try {
                return operation.get();
            } catch (RuntimeException e) {
                sleep.accept((int) (random.getAsDouble() * ceiling)); // full jitter: anywhere in [0, ceiling)
                ceiling = Math.min(capMs, ceiling * 2);
            }
        }
        return operation.get(); // the last attempt: its exception reaches the caller
    }

    static Supplier<String> flaky(int failures, String result) {
        int[] calls = {0};
        return () -> {
            calls[0]++;
            if (calls[0] <= failures) throw new IllegalStateException("timeout " + calls[0]);
            return result;
        };
    }

    static DoubleSupplier cycle(double... values) {
        int[] calls = {0};
        return () -> values[calls[0]++ % values.length];
    }

    static String failure(Supplier<String> op) {
        try {
            op.get();
            return "no error";
        } catch (IllegalStateException e) {
            return e.getMessage();
        }
    }

    public static void main(String[] args) {
        List<Integer> delays = new ArrayList<>();
        check(retry(flaky(2, "receipt-42"), 5, 100, 1000, delays::add, cycle(0.5)).equals("receipt-42"));
        check(delays.equals(List.of(50, 100)));

        delays.clear();
        Supplier<String> op = flaky(10, "never");
        check(failure(() -> retry(op, 4, 100, 250, delays::add, cycle(0.5, 0.25, 0.75))).equals("timeout 4"));
        check(delays.equals(List.of(50, 50, 187)));

        delays.clear();
        check(retry(flaky(0, "first try"), 3, 100, 1000, delays::add, cycle(0.5)).equals("first try"));
        check(delays.isEmpty());

        delays.clear();
        check(failure(() -> retry(flaky(1, "late"), 1, 100, 1000, delays::add, cycle(0.5))).equals("timeout 1"));
        check(delays.isEmpty());

        delays.clear();
        check(retry(flaky(6, "ok"), 7, 1000, 5000, delays::add, cycle(0.999)).equals("ok"));
        check(delays.equals(List.of(999, 1998, 3996, 4995, 4995, 4995)));
        System.out.println("197 ok");
    }

    static void check(boolean ok) {
        if (!ok) throw new AssertionError("test failed");
    }
}
