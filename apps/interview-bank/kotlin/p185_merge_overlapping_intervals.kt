// 185 · Merge Overlapping Intervals
package p185

fun mergeIntervals(intervals: Array<IntArray>): Array<IntArray> {
    val merged = mutableListOf<IntArray>()
    for (iv in intervals.sortedBy { it[0] }) {
        val last = merged.lastOrNull()
        if (last != null && iv[0] <= last[1]) {
            last[1] = maxOf(last[1], iv[1])
        } else {
            merged.add(intArrayOf(iv[0], iv[1])) // a new pair, so extending it never changes the input
        }
    }
    return merged.toTypedArray()
}

fun ivs(vararg pairs: Pair<Int, Int>): Array<IntArray> = pairs.map { intArrayOf(it.first, it.second) }.toTypedArray()

fun main() {
    check(mergeIntervals(ivs(9 to 12, 1 to 3, 2 to 6, 8 to 9)).contentDeepEquals(ivs(1 to 6, 8 to 12)))
    check(mergeIntervals(ivs(1 to 10, 2 to 3, 4 to 5)).contentDeepEquals(ivs(1 to 10)))
    check(mergeIntervals(ivs()).isEmpty())
    check(mergeIntervals(ivs(5 to 5)).contentDeepEquals(ivs(5 to 5)))
    check(mergeIntervals(ivs(3 to 4, 1 to 2)).contentDeepEquals(ivs(1 to 2, 3 to 4)))
    check(mergeIntervals(ivs(4 to 6, 1 to 4, 6 to 6)).contentDeepEquals(ivs(1 to 6)))
    println("185 ok")
}
