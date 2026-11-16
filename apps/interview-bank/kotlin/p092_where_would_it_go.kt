// 092 · Where Would It Go?
package p092

fun insertPosition(deadlines: IntArray, deadline: Int): Int {
    var lo = 0
    var hi = deadlines.size // the answer may be deadlines.size itself
    while (lo < hi) {
        val mid = lo + (hi - lo) / 2
        if (deadlines[mid] >= deadline) hi = mid else lo = mid + 1
    }
    return lo
}

fun main() {
    val stops = intArrayOf(480, 540, 600, 720)
    check(insertPosition(stops, 610) == 3)
    check(insertPosition(intArrayOf(480, 540, 540, 600), 540) == 1)
    check(insertPosition(intArrayOf(), 300) == 0)
    check(insertPosition(stops, 100) == 0)
    check(insertPosition(stops, 800) == 4)
    check(insertPosition(stops, 480) == 0)
    check(insertPosition(stops, 720) == 3)
    check(insertPosition(intArrayOf(5, 5, 5), 5) == 0)
    println("092 ok")
}
