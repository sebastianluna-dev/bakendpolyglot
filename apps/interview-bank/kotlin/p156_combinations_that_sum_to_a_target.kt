// 156 · Combinations That Sum to a Target
package p156

fun combinationsToTarget(candidates: IntArray, target: Int): List<List<Int>> {
    val sizes = candidates.sortedArray()
    val result = mutableListOf<List<Int>>()
    val path = mutableListOf<Int>()
    fun walk(start: Int, remaining: Int) {
        if (remaining == 0) {
            result.add(path.toList())
            return
        }
        for (i in start until sizes.size) {
            if (sizes[i] > remaining) break // sorted: every later size is too big as well
            path.add(sizes[i])
            walk(i, remaining - sizes[i]) // i, not i + 1: the same size may be used again
            path.removeAt(path.lastIndex)
        }
    }
    walk(0, target)
    return result
}

fun main() {
    check(combinationsToTarget(intArrayOf(2, 3, 5), 8) == listOf(listOf(2, 2, 2, 2), listOf(2, 3, 3), listOf(3, 5)))
    check(combinationsToTarget(intArrayOf(7, 3, 2), 7) == listOf(listOf(2, 2, 3), listOf(7)))
    check(combinationsToTarget(intArrayOf(4, 6), 5).isEmpty())
    check(combinationsToTarget(intArrayOf(1), 3) == listOf(listOf(1, 1, 1)))
    check(combinationsToTarget(intArrayOf(9), 9) == listOf(listOf(9)))
    check(combinationsToTarget(intArrayOf(5, 1), 6) == listOf(listOf(1, 1, 1, 1, 1, 1), listOf(1, 5)))
    println("156 ok")
}
