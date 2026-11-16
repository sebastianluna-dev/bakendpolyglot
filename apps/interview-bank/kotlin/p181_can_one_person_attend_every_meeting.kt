// 181 · Can One Person Attend Every Meeting?
package p181

fun canAttendAll(meetings: Array<IntArray>): Boolean {
    meetings.sortBy { it[0] }
    for (i in 1 until meetings.size) {
        if (meetings[i][0] < meetings[i - 1][1]) return false // starting just as the last one ends is fine
    }
    return true
}

fun main() {
    check(canAttendAll(arrayOf(intArrayOf(540, 600), intArrayOf(600, 660), intArrayOf(480, 540))))
    check(!canAttendAll(arrayOf(intArrayOf(540, 630), intArrayOf(600, 660))))
    check(canAttendAll(arrayOf()))
    check(canAttendAll(arrayOf(intArrayOf(100, 200))))
    check(!canAttendAll(arrayOf(intArrayOf(700, 800), intArrayOf(100, 200), intArrayOf(150, 160))))
    check(!canAttendAll(arrayOf(intArrayOf(5, 10), intArrayOf(5, 6))))
    check(canAttendAll(arrayOf(intArrayOf(0, 1), intArrayOf(2, 3), intArrayOf(1, 2))))
    println("181 ok")
}
