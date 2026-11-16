// 058 · Shortest Stretch Reaching a Total
package p058

fun shortestReaching(weights: IntArray, target: Int): Int {
    val n = weights.size
    var best = n + 1 // longer than any real stretch: means "none found"
    var left = 0
    var sum = 0
    for (right in 0 until n) {
        sum += weights[right]
        while (sum >= target) {
            best = minOf(best, right - left + 1)
            sum -= weights[left]
            left++
        }
    }
    return if (best == n + 1) 0 else best
}

fun main() {
    check(shortestReaching(intArrayOf(3, 1, 4, 1, 5, 9, 2), 15) == 3)
    check(shortestReaching(intArrayOf(2, 2, 2), 7) == 0)
    check(shortestReaching(intArrayOf(), 5) == 0)
    check(shortestReaching(intArrayOf(8), 8) == 1)
    check(shortestReaching(intArrayOf(1, 1, 1, 1), 4) == 4)
    check(shortestReaching(intArrayOf(1, 6, 1, 1, 7), 7) == 1)
    println("058 ok")
}
