// 142 · Smash the Heaviest Stones
package p142

import java.util.PriorityQueue

fun lastStone(stones: IntArray): Int {
    val heap = PriorityQueue<Int>(compareByDescending { it }) // heaviest first
    for (s in stones) heap.add(s)
    while (heap.size > 1) {
        val x = heap.poll()
        val y = heap.poll()
        if (x > y) heap.add(x - y)
    }
    return if (heap.isEmpty()) 0 else heap.peek()
}

fun main() {
    check(lastStone(intArrayOf(3, 9, 4, 4, 1)) == 1)
    check(lastStone(intArrayOf(10, 4, 4, 2)) == 0)
    check(lastStone(intArrayOf(6, 6)) == 0)
    check(lastStone(intArrayOf(7)) == 7)
    check(lastStone(intArrayOf(1, 1, 1)) == 1)
    check(lastStone(intArrayOf(2, 2, 3)) == 1)
    check(lastStone(intArrayOf(5, 1, 1, 1, 1, 1)) == 0)
    println("142 ok")
}
