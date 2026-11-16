// 051 · Best Average Over k Days
package p051

import kotlin.math.abs

fun bestAverage(balances: IntArray, k: Int): Double {
    var sum = 0
    for (i in 0 until k) sum += balances[i]
    var best = sum
    for (i in k until balances.size) {
        sum += balances[i] - balances[i - k] // the day entering minus the day leaving
        best = maxOf(best, sum)
    }
    return best.toDouble() / k
}

private fun close(got: Double, want: Double) = abs(got - want) < 1e-9

fun main() {
    check(close(bestAverage(intArrayOf(4, -2, 7, 5, -1, 6), 4), 4.25))
    check(close(bestAverage(intArrayOf(-3, -1, -4), 2), -2.0))
    check(close(bestAverage(intArrayOf(5), 1), 5.0))
    check(close(bestAverage(intArrayOf(1, 2, 3, 4), 1), 4.0))
    check(close(bestAverage(intArrayOf(-7, -7, -7), 3), -7.0))
    check(close(bestAverage(intArrayOf(1, 1, 2), 3), 4.0 / 3))
    println("051 ok")
}
