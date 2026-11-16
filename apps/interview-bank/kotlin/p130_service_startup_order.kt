// 130 · Service Startup Order
package p130

import java.util.PriorityQueue

fun startupOrder(n: Int, dependencies: Array<IntArray>): List<Int> {
    val dependents = List(n) { mutableListOf<Int>() }
    val waiting = IntArray(n) // dependencies not yet started
    for ((service, needs) in dependencies) {
        dependents[needs].add(service)
        waiting[service]++
    }
    val ready = PriorityQueue<Int>()
    for (s in 0..<n) {
        if (waiting[s] == 0) ready.add(s)
    }
    val order = mutableListOf<Int>()
    while (ready.isNotEmpty()) {
        val service = ready.poll()
        order.add(service)
        for (d in dependents[service]) {
            waiting[d]--
            if (waiting[d] == 0) ready.add(d)
        }
    }
    return if (order.size == n) order else listOf()
}

fun main() {
    check(startupOrder(5, arrayOf(intArrayOf(0, 3), intArrayOf(2, 4), intArrayOf(1, 0))) == listOf(3, 0, 1, 4, 2))
    check(startupOrder(3, arrayOf(intArrayOf(0, 1), intArrayOf(1, 2), intArrayOf(2, 0))).isEmpty())
    check(startupOrder(3, arrayOf()) == listOf(0, 1, 2))
    check(startupOrder(1, arrayOf()) == listOf(0))
    check(startupOrder(2, arrayOf(intArrayOf(1, 1))).isEmpty())
    check(startupOrder(3, arrayOf(intArrayOf(2, 0), intArrayOf(2, 0), intArrayOf(1, 2))) == listOf(0, 2, 1))
    check(startupOrder(4, arrayOf(intArrayOf(1, 0), intArrayOf(2, 3), intArrayOf(3, 2))).isEmpty())
    println("130 ok")
}
