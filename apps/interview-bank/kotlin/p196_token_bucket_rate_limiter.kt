// 196 · Token-Bucket Rate Limiter
package p196

class TokenBucket(capacity: Int, private val refillPerSecond: Int) {
    private val capacity = capacity * 1000L // in thousandths of a token, so the arithmetic stays exact
    private var tokens = this.capacity
    private var lastMs = 0L

    fun allow(nowMs: Long): Boolean {
        // one token per second is one thousandth per millisecond
        tokens = minOf(capacity, tokens + (nowMs - lastMs) * refillPerSecond)
        lastMs = nowMs
        if (tokens < 1000) return false
        tokens -= 1000
        return true
    }
}

fun allowAll(bucket: TokenBucket, vararg times: Long): List<Boolean> = times.map { bucket.allow(it) }

fun main() {
    check(
        allowAll(TokenBucket(3, 1), 0, 0, 0, 0, 999, 1000, 1000, 5000, 5000, 5000, 5000) ==
            listOf(true, true, true, false, false, true, false, true, true, true, false),
    )
    check(allowAll(TokenBucket(2, 4), 100, 100, 200, 350, 400, 600) == listOf(true, true, false, true, false, true))
    check(allowAll(TokenBucket(1, 1), 1_000_000_000, 1_000_000_000, 1_000_000_999, 1_000_001_000) == listOf(true, false, false, true))
    check(allowAll(TokenBucket(5, 10_000), 7, 7, 7, 7, 7, 7, 7) == listOf(true, true, true, true, true, false, false))
    println("196 ok")
}
