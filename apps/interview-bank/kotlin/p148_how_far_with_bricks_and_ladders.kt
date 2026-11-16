// 148 · How Far With Bricks and Ladders
package p148

import java.util.PriorityQueue

fun furthestPlatform(heights: IntArray, bricks: Int, ladders: Int): Int {
    val onLadders = PriorityQueue<Int>() // the climbs ladders cover so far
    var bricksLeft = bricks
    for (i in 0 until heights.size - 1) {
        val climb = heights[i + 1] - heights[i]
        if (climb <= 0) continue
        onLadders.add(climb)
        if (onLadders.size > ladders) bricksLeft -= onLadders.poll() // the smallest climb goes to bricks
        if (bricksLeft < 0) return i
    }
    return heights.size - 1
}

fun main() {
    check(furthestPlatform(intArrayOf(3, 8, 6, 10, 11, 15, 7), 4, 1) == 3)
    check(furthestPlatform(intArrayOf(10, 12, 13, 25, 26), 4, 1) == 4)
    check(furthestPlatform(intArrayOf(7), 0, 0) == 0)
    check(furthestPlatform(intArrayOf(9, 7, 7, 3), 0, 0) == 3)
    check(furthestPlatform(intArrayOf(1, 2), 0, 0) == 0)
    check(furthestPlatform(intArrayOf(1, 2, 3, 4), 0, 3) == 3)
    check(furthestPlatform(intArrayOf(1, 6, 2, 9, 4, 5), 13, 0) == 5)
    check(furthestPlatform(intArrayOf(1, 6, 2, 9, 4, 5), 12, 0) == 4)
    println("148 ok")
}
