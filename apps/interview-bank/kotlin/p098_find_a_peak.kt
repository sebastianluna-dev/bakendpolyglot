// 098 · Find a Peak
package p098

fun findPeak(altitudes: IntArray): Int {
    var lo = 0
    var hi = altitudes.size - 1 // the last index can be the answer, but has no right neighbour to test
    while (lo < hi) {
        val mid = lo + (hi - lo) / 2
        // Going down after mid: there is a peak at mid or to its left.
        if (altitudes[mid] > altitudes[mid + 1]) hi = mid else lo = mid + 1
    }
    return lo
}

fun isPeak(a: IntArray, i: Int): Boolean =
    i in a.indices && (i == 0 || a[i - 1] < a[i]) && (i == a.size - 1 || a[i + 1] < a[i])

fun main() {
    val routes = listOf(
        intArrayOf(120, 135, 150, 140, 160, 155),
        intArrayOf(10, 20, 30),
        intArrayOf(5),
        intArrayOf(3, 1),
        intArrayOf(1, 3),
        intArrayOf(5, 4, 3, 2, 1),
        intArrayOf(1, 3, 2, 4, 3, 5, 4),
        intArrayOf(1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 9),
    )
    for (route in routes) check(isPeak(route, findPeak(route)))
    println("098 ok")
}
