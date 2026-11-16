// 012 · Shared Members of Two Lists
package p012

fun sharedMembers(first: IntArray, second: IntArray): IntArray {
    val pending = second.toHashSet()
    val shared = mutableListOf<Int>()
    for (id in first) {
        if (pending.remove(id)) shared.add(id) // removed, so a repeat in first is skipped
    }
    return shared.toIntArray()
}

fun main() {
    check(sharedMembers(intArrayOf(104, 211, 104, 350, 87), intArrayOf(87, 104, 999, 104)).contentEquals(intArrayOf(104, 87)))
    check(sharedMembers(intArrayOf(1, 2), intArrayOf(3, 4)).isEmpty())
    check(sharedMembers(intArrayOf(), intArrayOf(1)).isEmpty())
    check(sharedMembers(intArrayOf(5, 5, 5), intArrayOf(5)).contentEquals(intArrayOf(5)))
    check(sharedMembers(intArrayOf(3, 1, 2), intArrayOf(2, 3)).contentEquals(intArrayOf(3, 2)))
    println("012 ok")
}
