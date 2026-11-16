// 187 · Fewest Removals to Avoid Overlap
package p187

fun fewestRemovals(intervals: Array<IntArray>): Int {
    var removals = 0
    var lastEnd = Int.MIN_VALUE // end of the last interval kept
    for (iv in intervals.sortedBy { it[1] }) {
        if (iv[0] >= lastEnd) {
            lastEnd = iv[1]
        } else {
            removals++
        }
    }
    return removals
}

fun ivs(vararg pairs: Pair<Int, Int>): Array<IntArray> = pairs.map { intArrayOf(it.first, it.second) }.toTypedArray()

fun main() {
    check(fewestRemovals(ivs(1 to 10, 2 to 3, 4 to 5, 6 to 7)) == 1)
    check(fewestRemovals(ivs(1 to 3, 3 to 5, 5 to 8)) == 0)
    check(fewestRemovals(ivs()) == 0)
    check(fewestRemovals(ivs(1 to 2, 1 to 2, 1 to 2)) == 2)
    check(fewestRemovals(ivs(1 to 4, 2 to 3, 3 to 6, 5 to 7)) == 2)
    println("187 ok")
}
