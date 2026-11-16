// 196 · Token-Bucket Rate Limiter
import java.util.Arrays;

public class P196TokenBucketRateLimiter {
    static class TokenBucket {
        private final long capacity;
        private final int refillPerSecond;
        private long tokens;
        private long lastMs = 0;

        TokenBucket(int capacity, int refillPerSecond) {
            this.capacity = capacity * 1000L; // in thousandths of a token, so the arithmetic stays exact
            this.refillPerSecond = refillPerSecond;
            this.tokens = this.capacity;
        }

        boolean allow(long nowMs) {
            // one token per second is one thousandth per millisecond
            tokens = Math.min(capacity, tokens + (nowMs - lastMs) * refillPerSecond);
            lastMs = nowMs;
            if (tokens < 1000) return false;
            tokens -= 1000;
            return true;
        }
    }

    static boolean[] allowAll(TokenBucket bucket, long... times) {
        boolean[] out = new boolean[times.length];
        for (int k = 0; k < times.length; k++) out[k] = bucket.allow(times[k]);
        return out;
    }

    public static void main(String[] args) {
        check(Arrays.equals(allowAll(new TokenBucket(3, 1), 0, 0, 0, 0, 999, 1000, 1000, 5000, 5000, 5000, 5000),
                new boolean[] {true, true, true, false, false, true, false, true, true, true, false}));
        check(Arrays.equals(allowAll(new TokenBucket(2, 4), 100, 100, 200, 350, 400, 600),
                new boolean[] {true, true, false, true, false, true}));
        check(Arrays.equals(allowAll(new TokenBucket(1, 1), 1_000_000_000, 1_000_000_000, 1_000_000_999, 1_000_001_000),
                new boolean[] {true, false, false, true}));
        check(Arrays.equals(allowAll(new TokenBucket(5, 10_000), 7, 7, 7, 7, 7, 7, 7),
                new boolean[] {true, true, true, true, true, false, false}));
        System.out.println("196 ok");
    }

    static void check(boolean ok) {
        if (!ok) throw new AssertionError("test failed");
    }
}
