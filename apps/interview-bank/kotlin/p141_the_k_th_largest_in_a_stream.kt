// 141 · The k-th Largest in a Stream
package p141

import java.util.PriorityQueue

class KthLargest(private val k: Int, nums: IntArray) {
    private val heap = PriorityQueue<Int>() // the k largest values so far; the smallest is on top

    init {
        for (x in nums) add(x)
    }

    fun add(value: Int): Int {
        heap.add(value)
        if (heap.size > k) heap.poll() // drop the smallest: it is no longer in the top k
        return heap.peek()
    }
}

fun main() {
    val board = KthLargest(3, intArrayOf(40, 75, 20, 90))
    check(board.add(50) == 50)
    check(board.add(10) == 50)
    check(board.add(80) == 75)
    check(board.add(95) == 80)

    val single = KthLargest(1, intArrayOf())
    check(single.add(-3) == -3)
    check(single.add(-5) == -3)
    check(single.add(2) == 2)

    val ties = KthLargest(2, intArrayOf(7))
    check(ties.add(7) == 7)
    check(ties.add(3) == 7)
    check(ties.add(9) == 7)
    check(ties.add(8) == 8)

    val many = KthLargest(4, intArrayOf(5, 1, 9, 3, 7, 2))
    check(many.add(4) == 4)
    check(many.add(6) == 5)
    check(many.add(10) == 6)
    println("141 ok")
}
