// 196 · Token-Bucket Rate Limiter
Check(AllowAll(new TokenBucket(3, 1), [0, 0, 0, 0, 999, 1000, 1000, 5000, 5000, 5000, 5000])
    .SequenceEqual([true, true, true, false, false, true, false, true, true, true, false]));
Check(AllowAll(new TokenBucket(2, 4), [100, 100, 200, 350, 400, 600]).SequenceEqual([true, true, false, true, false, true]));
Check(AllowAll(new TokenBucket(1, 1), [1_000_000_000, 1_000_000_000, 1_000_000_999, 1_000_001_000])
    .SequenceEqual([true, false, false, true]));
Check(AllowAll(new TokenBucket(5, 10_000), [7, 7, 7, 7, 7, 7, 7]).SequenceEqual([true, true, true, true, true, false, false]));
Console.WriteLine("196 ok");

static bool[] AllowAll(TokenBucket bucket, long[] times) => times.Select(bucket.Allow).ToArray();

static void Check(bool ok)
{
    if (!ok) throw new Exception("test failed");
}

class TokenBucket(int capacity, int refillPerSecond)
{
    private readonly long max = capacity * 1000L; // in thousandths of a token, so the arithmetic stays exact
    private long tokens = capacity * 1000L;
    private long lastMs;

    public bool Allow(long nowMs)
    {
        // one token per second is one thousandth per millisecond
        tokens = Math.Min(max, tokens + (nowMs - lastMs) * refillPerSecond);
        lastMs = nowMs;
        if (tokens < 1000) return false;
        tokens -= 1000;
        return true;
    }
}
