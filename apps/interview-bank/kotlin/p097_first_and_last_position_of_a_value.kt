// 097 · First and Last Position of a Value
package p097

fun firstAndLast(days: IntArray, day: Int): IntArray {
    val first = firstAtLeast(days, day)
    if (first == days.size || days[first] != day) return intArrayOf(-1, -1)
    return intArrayOf(first, firstAtLeast(days, day + 1) - 1) // one before the first value >= day + 1
}

fun firstAtLeast(a: IntArray, target: Int): Int {
    var lo = 0
    var hi = a.size
    while (lo < hi) {
        val mid = lo + (hi - lo) / 2
        if (a[mid] >= target) hi = mid else lo = mid + 1
    }
    return lo
}

fun main() {
    val due = intArrayOf(3, 5, 5, 5, 8, 9)
    val none = intArrayOf(-1, -1)
    check(firstAndLast(due, 5).contentEquals(intArrayOf(1, 3)))
    check(firstAndLast(intArrayOf(3, 5, 8), 6).contentEquals(none))
    check(firstAndLast(intArrayOf(), 1).contentEquals(none))
    check(firstAndLast(intArrayOf(7, 7, 7, 7), 7).contentEquals(intArrayOf(0, 3)))
    check(firstAndLast(intArrayOf(1), 1).contentEquals(intArrayOf(0, 0)))
    check(firstAndLast(intArrayOf(1, 2, 2), 2).contentEquals(intArrayOf(1, 2)))
    check(firstAndLast(due, 1).contentEquals(none))
    check(firstAndLast(due, 10).contentEquals(none))
    check(firstAndLast(due, 3).contentEquals(intArrayOf(0, 0)))
    check(firstAndLast(due, 9).contentEquals(intArrayOf(5, 5)))
    println("097 ok")
}
