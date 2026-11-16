// 065 · Maximum of Every Window
package p065

fun windowMaxima(readings: IntArray, k: Int): IntArray {
    val result = IntArray(maxOf(0, readings.size - k + 1))
    val window = ArrayDeque<Int>() // indices; their readings decrease from first to last
    for (i in readings.indices) {
        while (window.isNotEmpty() && readings[window.last()] <= readings[i]) window.removeLast()
        window.addLast(i)
        if (window.first() <= i - k) window.removeFirst()
        if (i >= k - 1) result[i - k + 1] = readings[window.first()]
    }
    return result
}

fun main() {
    check(windowMaxima(intArrayOf(3, 1, -2, 5, 4, 2, 6), 3).contentEquals(intArrayOf(3, 5, 5, 5, 6)))
    check(windowMaxima(intArrayOf(2, 7, 1), 3).contentEquals(intArrayOf(7)))
    check(windowMaxima(intArrayOf(4, -1, 4), 1).contentEquals(intArrayOf(4, -1, 4)))
    check(windowMaxima(intArrayOf(), 2).isEmpty())
    check(windowMaxima(intArrayOf(1, 2), 3).isEmpty())
    check(windowMaxima(intArrayOf(5, 5, 1, 5), 2).contentEquals(intArrayOf(5, 5, 5)))
    check(windowMaxima(intArrayOf(9, 7, 5, 3), 2).contentEquals(intArrayOf(9, 7, 5)))
    check(windowMaxima(intArrayOf(9, 1, 1, 1), 2).contentEquals(intArrayOf(9, 1, 1)))
    println("065 ok")
}
