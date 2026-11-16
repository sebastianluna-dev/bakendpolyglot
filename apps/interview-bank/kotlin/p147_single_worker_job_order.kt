// 147 · Single-Worker Job Order
package p147

import java.util.PriorityQueue

fun jobOrder(jobs: Array<IntArray>): IntArray {
    val byArrival = jobs.indices.sortedBy { jobs[it][0] }
    // job indices: shortest duration first, then the smaller index
    val ready = PriorityQueue<Int>(compareBy({ jobs[it][1] }, { it }))
    val order = IntArray(jobs.size)
    var time = 0L // can pass Int.MAX_VALUE
    var next = 0 // position in byArrival of the first job not yet queued
    for (done in jobs.indices) {
        if (ready.isEmpty() && time < jobs[byArrival[next]][0]) time = jobs[byArrival[next]][0].toLong() // idle
        while (next < jobs.size && jobs[byArrival[next]][0] <= time) ready.add(byArrival[next++])
        val j = ready.poll()
        time += jobs[j][1]
        order[done] = j
    }
    return order
}

fun jobs(vararg j: Pair<Int, Int>): Array<IntArray> = j.map { intArrayOf(it.first, it.second) }.toTypedArray()

fun main() {
    check(jobOrder(jobs(1 to 3, 2 to 1, 3 to 2, 4 to 1)).contentEquals(intArrayOf(0, 1, 3, 2)))
    check(jobOrder(jobs(5 to 2, 5 to 2, 5 to 1, 20 to 1)).contentEquals(intArrayOf(2, 0, 1, 3)))
    check(jobOrder(jobs(7 to 4)).contentEquals(intArrayOf(0)))
    check(jobOrder(jobs(1 to 2, 3 to 5, 3 to 1)).contentEquals(intArrayOf(0, 2, 1)))
    check(jobOrder(jobs(9 to 1, 1 to 1)).contentEquals(intArrayOf(1, 0)))
    check(jobOrder(jobs(1 to 1_000_000_000, 1 to 1_000_000_000, 1_000_000_000 to 1)).contentEquals(intArrayOf(0, 2, 1)))
    println("147 ok")
}
