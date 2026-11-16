// 017 · Longest Run of Consecutive IDs
package p017

fun longestRun(ids: IntArray): Int {
    val present = ids.toHashSet()
    var best = 0
    for (id in present) {
        if (id - 1 in present) continue // not the start of a run
        var length = 1
        while (id + length in present) length++
        best = maxOf(best, length)
    }
    return best
}

fun main() {
    check(longestRun(intArrayOf(104, 7, 101, 5, 103, 6, 102)) == 4)
    check(longestRun(intArrayOf(9, 1, 9, 2, 2)) == 2)
    check(longestRun(intArrayOf()) == 0)
    check(longestRun(intArrayOf(5)) == 1)
    check(longestRun(intArrayOf(-1, 0, 1, 3)) == 3)
    check(longestRun(intArrayOf(10, 30, 20)) == 1)
    println("017 ok")
}
