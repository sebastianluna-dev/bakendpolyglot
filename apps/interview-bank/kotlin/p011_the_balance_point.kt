// 011 · The Balance Point
package p011

fun balancePoint(nums: IntArray): Int {
    val total = nums.sum()
    var left = 0
    for ((i, x) in nums.withIndex()) {
        if (left == total - left - x) return i
        left += x
    }
    return -1
}

fun main() {
    check(balancePoint(intArrayOf(3, 1, 5, 2, 2)) == 2)
    check(balancePoint(intArrayOf(1, 2, 3)) == -1)
    check(balancePoint(intArrayOf()) == -1)
    check(balancePoint(intArrayOf(7)) == 0)
    check(balancePoint(intArrayOf(5, 2, -2)) == 0)
    check(balancePoint(intArrayOf(0, 0, 0)) == 0)
    println("011 ok")
}
