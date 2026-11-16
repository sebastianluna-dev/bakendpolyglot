// 073 · Days Until a Warmer Day
package p073

fun daysUntilWarmer(temps: IntArray): IntArray {
    val wait = IntArray(temps.size)
    val stack = ArrayDeque<Int>() // indices still waiting, their temperatures never rising from bottom to top
    for (i in temps.indices) {
        while (stack.isNotEmpty() && temps[stack.last()] < temps[i]) {
            val j = stack.removeLast()
            wait[j] = i - j
        }
        stack.addLast(i)
    }
    return wait
}

fun main() {
    check(daysUntilWarmer(intArrayOf(18, 16, 17, 21, 20, 20, 23)).contentEquals(intArrayOf(3, 1, 1, 3, 2, 1, 0)))
    check(daysUntilWarmer(intArrayOf(30, 25, 20)).contentEquals(intArrayOf(0, 0, 0)))
    check(daysUntilWarmer(intArrayOf()).isEmpty())
    check(daysUntilWarmer(intArrayOf(12)).contentEquals(intArrayOf(0)))
    check(daysUntilWarmer(intArrayOf(5, 5, 5)).contentEquals(intArrayOf(0, 0, 0)))
    check(daysUntilWarmer(intArrayOf(-3, -1, 0, 2)).contentEquals(intArrayOf(1, 1, 1, 0)))
    println("073 ok")
}
