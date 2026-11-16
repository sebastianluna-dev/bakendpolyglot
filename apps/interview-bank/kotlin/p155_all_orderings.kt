// 155 · All Orderings
package p155

fun allOrderings(nums: IntArray): List<List<Int>> {
    val result = mutableListOf<List<Int>>()
    val path = mutableListOf<Int>()
    val used = BooleanArray(nums.size)
    fun walk() {
        if (path.size == nums.size) {
            result.add(path.toList())
            return
        }
        for (i in nums.indices) {
            if (used[i]) continue
            used[i] = true
            path.add(nums[i])
            walk()
            path.removeAt(path.lastIndex)
            used[i] = false
        }
    }
    walk()
    return result
}

fun main() {
    check(allOrderings(intArrayOf(1, 2, 3)) == listOf(listOf(1, 2, 3), listOf(1, 3, 2), listOf(2, 1, 3), listOf(2, 3, 1), listOf(3, 1, 2), listOf(3, 2, 1)))
    check(allOrderings(intArrayOf(3, 1, 2)) == listOf(listOf(3, 1, 2), listOf(3, 2, 1), listOf(1, 3, 2), listOf(1, 2, 3), listOf(2, 3, 1), listOf(2, 1, 3)))
    check(allOrderings(intArrayOf()) == listOf(listOf<Int>()))
    check(allOrderings(intArrayOf(9)) == listOf(listOf(9)))
    check(allOrderings(intArrayOf(5, -5)) == listOf(listOf(5, -5), listOf(-5, 5)))
    check(allOrderings(intArrayOf(1, 2, 3, 4, 5, 6)).size == 720)
    println("155 ok")
}
