// 193 · Cursor Pagination Over a Sorted List
package p193

data class Page(val items: List<Int>, val next: Int?)

fun fetchPage(ids: IntArray, after: Int?, limit: Int): Page {
    var start = 0
    if (after != null) {
        var lo = 0
        var hi = ids.size // looking for the first id greater than after
        while (lo < hi) {
            val mid = lo + (hi - lo) / 2
            if (ids[mid] > after) hi = mid else lo = mid + 1
        }
        start = lo
    }
    val end = minOf(start + limit, ids.size)
    val items = ids.slice(start until end)
    return Page(items, if (end < ids.size) items.last() else null)
}

fun main() {
    val ids = intArrayOf(3, 8, 15, 21, 30, 42, 57)
    check(fetchPage(ids, null, 3) == Page(listOf(3, 8, 15), 15))
    check(fetchPage(ids, 15, 3) == Page(listOf(21, 30, 42), 42))
    check(fetchPage(ids, 42, 3) == Page(listOf(57), null))
    check(fetchPage(ids, 20, 3) == Page(listOf(21, 30, 42), 42))
    check(fetchPage(ids, 57, 3) == Page(listOf(), null))
    check(fetchPage(ids, 1, 10) == Page(listOf(3, 8, 15, 21, 30, 42, 57), null))
    check(fetchPage(intArrayOf(3, 8, 15, 21, 30, 42), 15, 3) == Page(listOf(21, 30, 42), null))
    check(fetchPage(intArrayOf(), null, 5) == Page(listOf(), null))
    println("193 ok")
}
