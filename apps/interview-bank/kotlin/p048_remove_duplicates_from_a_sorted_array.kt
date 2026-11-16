// 048 · Remove Duplicates From a Sorted Array
package p048

fun removeDuplicates(ids: IntArray): Int {
    if (ids.isEmpty()) return 0
    var write = 1 // ids[0 until write] holds the distinct ids kept so far
    for (read in 1 until ids.size) {
        if (ids[read] != ids[write - 1]) ids[write++] = ids[read]
    }
    return write
}

fun deduped(ids: IntArray): IntArray {
    val k = removeDuplicates(ids)
    return ids.copyOf(k)
}

fun main() {
    check(deduped(intArrayOf(3, 3, 5, 8, 8, 8, 9)).contentEquals(intArrayOf(3, 5, 8, 9)))
    check(deduped(intArrayOf(7, 7, 7)).contentEquals(intArrayOf(7)))
    check(deduped(intArrayOf()).isEmpty())
    check(deduped(intArrayOf(1, 2, 3)).contentEquals(intArrayOf(1, 2, 3)))
    check(deduped(intArrayOf(10, 10, 20, 20, 30)).contentEquals(intArrayOf(10, 20, 30)))
    println("048 ok")
}
