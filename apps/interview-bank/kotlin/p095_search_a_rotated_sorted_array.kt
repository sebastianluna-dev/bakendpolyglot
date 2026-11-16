// 095 · Search a Rotated Sorted Array
package p095

fun searchRotated(times: IntArray, target: Int): Int {
    val n = times.size
    if (n == 0) return -1
    val last = times[n - 1]
    var lo = 0
    var hi = n
    while (lo < hi) {
        val mid = lo + (hi - lo) / 2
        if (times[mid] <= last) hi = mid else lo = mid + 1
    }
    val start = lo // index of the minimum: [start, n) and [0, start) are both sorted
    val (from, to) = if (target <= last) start to n else 0 to start
    val i = firstAtLeast(times, from, to, target)
    return if (i < to && times[i] == target) i else -1
}

fun firstAtLeast(a: IntArray, from: Int, to: Int, target: Int): Int {
    var lo = from
    var hi = to
    while (lo < hi) {
        val mid = lo + (hi - lo) / 2
        if (a[mid] >= target) hi = mid else lo = mid + 1
    }
    return lo
}

fun main() {
    val board = intArrayOf(900, 1020, 1200, 60, 300, 480)
    check(searchRotated(board, 300) == 4)
    check(searchRotated(board, 600) == -1)
    check(searchRotated(board, 1020) == 1)
    check(searchRotated(board, 900) == 0)
    check(searchRotated(board, 480) == 5)
    check(searchRotated(board, 60) == 3)
    check(searchRotated(board, 1300) == -1)
    check(searchRotated(board, 10) == -1)
    check(searchRotated(intArrayOf(1, 2, 3), 3) == 2)
    check(searchRotated(intArrayOf(5), 5) == 0)
    check(searchRotated(intArrayOf(5), 1) == -1)
    check(searchRotated(intArrayOf(), 1) == -1)
    check(searchRotated(intArrayOf(2, 1), 1) == 1)
    check(searchRotated(intArrayOf(2, 1), 2) == 0)
    println("095 ok")
}
