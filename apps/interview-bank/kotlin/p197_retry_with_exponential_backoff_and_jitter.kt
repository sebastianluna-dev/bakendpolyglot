// 197 · Retry With Exponential Backoff and Jitter
package p197

fun <T> retry(
    operation: () -> T,
    maxAttempts: Int,
    baseDelayMs: Int,
    capMs: Int,
    sleep: (Int) -> Unit,
    random: () -> Double,
): T {
    var ceiling = baseDelayMs
    for (attempt in 1 until maxAttempts) {
        try {
            return operation()
        } catch (e: Exception) {
            sleep((random() * ceiling).toInt()) // full jitter: anywhere in [0, ceiling)
            ceiling = minOf(capMs, ceiling * 2)
        }
    }
    return operation() // the last attempt: its exception reaches the caller
}

fun flaky(failures: Int, result: String): () -> String {
    var calls = 0
    return {
        calls++
        if (calls <= failures) throw IllegalStateException("timeout $calls")
        result
    }
}

fun cycle(vararg values: Double): () -> Double {
    var calls = 0
    return { values[calls++ % values.size] }
}

fun failure(op: () -> String): String =
    try {
        op()
        "no error"
    } catch (e: IllegalStateException) {
        e.message ?: ""
    }

fun main() {
    val delays = mutableListOf<Int>()
    check(retry(flaky(2, "receipt-42"), 5, 100, 1000, { delays.add(it) }, cycle(0.5)) == "receipt-42")
    check(delays == listOf(50, 100))

    delays.clear()
    val op = flaky(10, "never")
    check(failure { retry(op, 4, 100, 250, { delays.add(it) }, cycle(0.5, 0.25, 0.75)) } == "timeout 4")
    check(delays == listOf(50, 50, 187))

    delays.clear()
    check(retry(flaky(0, "first try"), 3, 100, 1000, { delays.add(it) }, cycle(0.5)) == "first try")
    check(delays.isEmpty())

    delays.clear()
    check(failure { retry(flaky(1, "late"), 1, 100, 1000, { delays.add(it) }, cycle(0.5)) } == "timeout 1")
    check(delays.isEmpty())

    delays.clear()
    check(retry(flaky(6, "ok"), 7, 1000, 5000, { delays.add(it) }, cycle(0.999)) == "ok")
    check(delays == listOf(999, 1998, 3996, 4995, 4995, 4995))
    println("197 ok")
}
