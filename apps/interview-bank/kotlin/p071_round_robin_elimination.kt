// 071 · Round-Robin Elimination
package p071

fun eliminationOrder(n: Int, k: Int): IntArray {
    val circle = ArrayDeque((1..n).toList())
    val order = mutableListOf<Int>()
    while (circle.isNotEmpty()) {
        val steps = (k - 1) % circle.size // skip whole laps when k is larger than the circle
        repeat(steps) { circle.addLast(circle.removeFirst()) }
        order.add(circle.removeFirst())
    }
    return order.toIntArray()
}

fun main() {
    check(eliminationOrder(5, 2).contentEquals(intArrayOf(2, 4, 1, 5, 3)))
    check(eliminationOrder(4, 3).contentEquals(intArrayOf(3, 2, 4, 1)))
    check(eliminationOrder(1, 7).contentEquals(intArrayOf(1)))
    check(eliminationOrder(4, 1).contentEquals(intArrayOf(1, 2, 3, 4)))
    check(eliminationOrder(3, 5).contentEquals(intArrayOf(2, 3, 1)))
    check(eliminationOrder(6, 6).contentEquals(intArrayOf(6, 1, 3, 2, 5, 4)))
    println("071 ok")
}
