// 199 · Bounded Worker Pool With Ordered Results
package p199

import java.util.concurrent.Callable
import java.util.concurrent.Executors

fun <T, R> mapLimit(items: List<T>, limit: Int, fn: (T) -> R): List<R> =
    Executors.newFixedThreadPool(limit).use { pool -> // the pool size is the limit
        val futures = items.map { item -> pool.submit(Callable { fn(item) }) }
        futures.map { it.get() } // in input order; get() rethrows a task's failure
    }

fun measure(durations: List<Int>, limit: Int): Pair<List<Int>, Int> {
    val lock = Any()
    var running = 0
    var peak = 0
    val results = mapLimit(durations, limit) { ms ->
        synchronized(lock) {
            running++
            peak = maxOf(peak, running)
        }
        Thread.sleep(ms.toLong())
        synchronized(lock) { running-- }
        ms * 10
    }
    return results to peak
}

fun main() {
    val durations = listOf(40, 20, 30, 20, 25, 20, 30, 20) // later tasks often finish before earlier ones
    check(measure(durations, 3) == listOf(400, 200, 300, 200, 250, 200, 300, 200) to 3)
    check(measure(listOf(20, 20, 20), 1) == listOf(200, 200, 200) to 1)
    check(measure(listOf(30, 20), 10) == listOf(300, 200) to 2)
    check(measure(listOf(), 4) == listOf<Int>() to 0)
    println("199 ok")
}
