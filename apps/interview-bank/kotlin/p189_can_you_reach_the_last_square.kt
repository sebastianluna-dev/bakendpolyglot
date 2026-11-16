// 189 · Can You Reach the Last Square?
package p189

fun canReachLast(jumps: IntArray): Boolean {
    var furthest = 0
    for ((i, jump) in jumps.withIndex()) {
        if (i > furthest) return false // no reachable square jumps this far
        furthest = maxOf(furthest, i + jump)
    }
    return true
}

fun main() {
    check(canReachLast(intArrayOf(2, 0, 3, 0, 0, 1)))
    check(!canReachLast(intArrayOf(3, 1, 1, 0, 4)))
    check(canReachLast(intArrayOf(0)))
    check(!canReachLast(intArrayOf(0, 1)))
    check(canReachLast(intArrayOf(4, 0, 0, 0, 0)))
    check(!canReachLast(intArrayOf(1, 1, 0, 1)))
    check(canReachLast(intArrayOf(1, 2, 0, 0)))
    println("189 ok")
}
