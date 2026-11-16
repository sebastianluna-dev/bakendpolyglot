// 013 · Collapse Consecutive IDs Into Ranges
package p013

fun collapseRanges(ids: IntArray): List<String> {
    val ranges = mutableListOf<String>()
    var i = 0
    while (i < ids.size) {
        val start = ids[i]
        while (i + 1 < ids.size && ids[i + 1] == ids[i] + 1) i++
        ranges.add(if (start == ids[i]) "$start" else "$start-${ids[i]}")
        i++
    }
    return ranges
}

fun main() {
    check(collapseRanges(intArrayOf(1, 2, 3, 7, 9, 10)) == listOf("1-3", "7", "9-10"))
    check(collapseRanges(intArrayOf(0, 2, 4)) == listOf("0", "2", "4"))
    check(collapseRanges(intArrayOf()).isEmpty())
    check(collapseRanges(intArrayOf(5)) == listOf("5"))
    check(collapseRanges(intArrayOf(40, 41, 42, 43)) == listOf("40-43"))
    println("013 ok")
}
