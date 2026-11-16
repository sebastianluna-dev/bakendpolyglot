// 154 · All Subsets
package p154

fun allSubsets(nums: IntArray): List<List<Int>> {
    val result = mutableListOf<List<Int>>()
    val path = mutableListOf<Int>()
    fun walk(start: Int) {
        result.add(path.toList()) // a copy: path keeps changing
        for (i in start until nums.size) {
            path.add(nums[i])
            walk(i + 1)
            path.removeAt(path.lastIndex)
        }
    }
    walk(0)
    return result
}

fun main() {
    check(allSubsets(intArrayOf(1, 2, 3)) == listOf(listOf(), listOf(1), listOf(1, 2), listOf(1, 2, 3), listOf(1, 3), listOf(2), listOf(2, 3), listOf(3)))
    check(allSubsets(intArrayOf(4, 1)) == listOf(listOf(), listOf(4), listOf(4, 1), listOf(1)))
    check(allSubsets(intArrayOf()) == listOf(listOf<Int>()))
    check(allSubsets(intArrayOf(7)) == listOf(listOf(), listOf(7)))
    check(allSubsets(intArrayOf(-1, 0, 2)) == listOf(listOf(), listOf(-1), listOf(-1, 0), listOf(-1, 0, 2), listOf(-1, 2), listOf(0), listOf(0, 2), listOf(2)))
    check(allSubsets(IntArray(10) { it }).size == 1024)
    println("154 ok")
}
