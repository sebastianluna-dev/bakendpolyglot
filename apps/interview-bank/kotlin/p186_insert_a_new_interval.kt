// 186 · Insert a New Interval
package p186

fun insertInterval(intervals: Array<IntArray>, newInterval: IntArray): Array<IntArray> {
    val result = mutableListOf<IntArray>()
    var start = newInterval[0]
    var end = newInterval[1]
    var i = 0
    while (i < intervals.size && intervals[i][1] < start) result.add(intervals[i++])
    while (i < intervals.size && intervals[i][0] <= end) {
        start = minOf(start, intervals[i][0])
        end = maxOf(end, intervals[i][1])
        i++
    }
    result.add(intArrayOf(start, end))
    while (i < intervals.size) result.add(intervals[i++])
    return result.toTypedArray()
}

fun ivs(vararg pairs: Pair<Int, Int>): Array<IntArray> = pairs.map { intArrayOf(it.first, it.second) }.toTypedArray()

fun main() {
    check(insertInterval(ivs(1 to 3, 6 to 8, 10 to 12, 15 to 18), intArrayOf(7, 10)).contentDeepEquals(ivs(1 to 3, 6 to 12, 15 to 18)))
    check(insertInterval(ivs(2 to 4, 9 to 11), intArrayOf(5, 7)).contentDeepEquals(ivs(2 to 4, 5 to 7, 9 to 11)))
    check(insertInterval(ivs(), intArrayOf(4, 6)).contentDeepEquals(ivs(4 to 6)))
    check(insertInterval(ivs(5 to 6), intArrayOf(1, 2)).contentDeepEquals(ivs(1 to 2, 5 to 6)))
    check(insertInterval(ivs(1 to 2), intArrayOf(7, 9)).contentDeepEquals(ivs(1 to 2, 7 to 9)))
    check(insertInterval(ivs(3 to 4, 6 to 7), intArrayOf(1, 10)).contentDeepEquals(ivs(1 to 10)))
    check(insertInterval(ivs(1 to 3), intArrayOf(3, 5)).contentDeepEquals(ivs(1 to 5)))
    check(insertInterval(ivs(5 to 8), intArrayOf(2, 5)).contentDeepEquals(ivs(2 to 8)))
    println("186 ok")
}
