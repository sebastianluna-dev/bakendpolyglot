// 134 · Time for a Signal to Reach Every Node
package p134

import java.util.PriorityQueue

fun signalTime(n: Int, links: Array<IntArray>, source: Int): Int {
    val graph = List(n) { mutableListOf<Pair<Int, Int>>() } // (to, ms)
    for ((from, dest, ms) in links) graph[from].add(dest to ms)
    val dist = IntArray(n) { Int.MAX_VALUE }
    dist[source] = 0
    val heap = PriorityQueue<Pair<Int, Int>>(compareBy { it.first }) // (distance, node)
    heap.add(0 to source)
    while (heap.isNotEmpty()) {
        val (d, node) = heap.poll()
        if (d > dist[node]) continue // stale: a shorter path was found after this was queued
        for ((next, ms) in graph[node]) {
            if (d + ms < dist[next]) {
                dist[next] = d + ms
                heap.add(dist[next] to next)
            }
        }
    }
    val slowest = dist.max()
    return if (slowest == Int.MAX_VALUE) -1 else slowest
}

fun main() {
    check(
        signalTime(
            4,
            arrayOf(intArrayOf(0, 1, 4), intArrayOf(0, 2, 1), intArrayOf(2, 1, 2), intArrayOf(1, 3, 1), intArrayOf(2, 3, 6)),
            0,
        ) == 4,
    )
    check(signalTime(3, arrayOf(intArrayOf(0, 1, 2), intArrayOf(2, 1, 1)), 0) == -1)
    check(signalTime(1, arrayOf(), 0) == 0)
    check(signalTime(2, arrayOf(intArrayOf(0, 1, 5), intArrayOf(0, 1, 2)), 0) == 2)
    check(signalTime(2, arrayOf(intArrayOf(0, 1, 3)), 1) == -1)
    check(signalTime(2, arrayOf(intArrayOf(1, 0, 3)), 1) == 3)
    check(signalTime(3, arrayOf(intArrayOf(0, 1, 1), intArrayOf(1, 2, 1), intArrayOf(2, 0, 1)), 1) == 2)
    check(signalTime(3, arrayOf(intArrayOf(0, 1, 0), intArrayOf(1, 2, 0)), 0) == 0)
    println("134 ok")
}
