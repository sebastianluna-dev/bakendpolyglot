// 096 · Minimum of a Rotated Sorted Array
package p096

fun rotatedMinimum(times: IntArray): Int {
    val last = times.last()
    var lo = 0
    var hi = times.size
    while (lo < hi) {
        val mid = lo + (hi - lo) / 2
        // Values <= last belong to the run after the wrap, which starts at the minimum.
        if (times[mid] <= last) hi = mid else lo = mid + 1
    }
    return times[lo]
}

fun main() {
    check(rotatedMinimum(intArrayOf(900, 1020, 1200, 60, 300, 480)) == 60)
    check(rotatedMinimum(intArrayOf(60, 300, 480)) == 60)
    check(rotatedMinimum(intArrayOf(7)) == 7)
    check(rotatedMinimum(intArrayOf(2, 1)) == 1)
    check(rotatedMinimum(intArrayOf(3, 4, 5, 1, 2)) == 1)
    check(rotatedMinimum(intArrayOf(2, 3, 4, 5, 1)) == 1)
    check(rotatedMinimum(intArrayOf(5, 1, 2, 3, 4)) == 1)
    println("096 ok")
}
