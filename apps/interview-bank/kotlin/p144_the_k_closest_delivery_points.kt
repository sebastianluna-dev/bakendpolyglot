// 144 · The k Closest Delivery Points
package p144

import java.util.PriorityQueue

fun kClosest(points: Array<IntArray>, k: Int): Array<IntArray> {
    val closer = compareBy<IntArray>({ it[0] * it[0] + it[1] * it[1] }, { it[0] }, { it[1] })
    val heap = PriorityQueue(closer.reversed()) // farthest kept point on top
    for (p in points) {
        heap.add(p)
        if (heap.size > k) heap.poll()
    }
    val result = Array(heap.size) { IntArray(0) }
    for (i in result.indices.reversed()) result[i] = heap.poll()
    return result
}

fun points(vararg p: Pair<Int, Int>): Array<IntArray> = p.map { intArrayOf(it.first, it.second) }.toTypedArray()

fun main() {
    check(kClosest(points(3 to 4, 1 to -2, -2 to 1, 0 to 5, 2 to 2), 3).contentDeepEquals(points(-2 to 1, 1 to -2, 2 to 2)))
    check(kClosest(points(2 to 0, 0 to 2, 0 to -2, -2 to 0), 2).contentDeepEquals(points(-2 to 0, 0 to -2)))
    check(kClosest(points(5 to 5, 1 to 1), 0).isEmpty())
    check(kClosest(points(6 to 1, -1 to -1, 0 to 0), 3).contentDeepEquals(points(0 to 0, -1 to -1, 6 to 1)))
    check(kClosest(points(1 to 1, 0 to 3, 1 to 1), 2).contentDeepEquals(points(1 to 1, 1 to 1)))
    check(kClosest(points(10000 to -10000, -10000 to 10000), 1).contentDeepEquals(points(-10000 to 10000)))
    println("144 ok")
}
