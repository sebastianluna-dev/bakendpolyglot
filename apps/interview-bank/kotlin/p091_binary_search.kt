// 091 · Binary Search
package p091

fun binarySearch(ids: IntArray, target: Int): Int {
    var lo = 0
    var hi = ids.size // the first ID >= target is somewhere in [lo, hi)
    while (lo < hi) {
        val mid = lo + (hi - lo) / 2
        if (ids[mid] >= target) hi = mid else lo = mid + 1
    }
    return if (lo < ids.size && ids[lo] == target) lo else -1
}

fun main() {
    val ids = intArrayOf(3, 8, 15, 21, 40, 57)
    check(binarySearch(ids, 21) == 3)
    check(binarySearch(ids, 10) == -1)
    check(binarySearch(intArrayOf(), 5) == -1)
    check(binarySearch(intArrayOf(7), 7) == 0)
    check(binarySearch(ids, 3) == 0)
    check(binarySearch(ids, 57) == 5)
    check(binarySearch(ids, 1) == -1)
    check(binarySearch(ids, 99) == -1)
    println("091 ok")
}
