// 016 · Product of Everything Else
package p016

fun productOfOthers(nums: IntArray): IntArray {
    val out = IntArray(nums.size)
    var left = 1
    for (i in nums.indices) {
        out[i] = left // product of everything before i
        left *= nums[i]
    }
    var right = 1
    for (i in nums.indices.reversed()) {
        out[i] *= right
        right *= nums[i]
    }
    return out
}

fun main() {
    check(productOfOthers(intArrayOf(2, 3, 4, 5)).contentEquals(intArrayOf(60, 40, 30, 24)))
    check(productOfOthers(intArrayOf(4, 0, 5)).contentEquals(intArrayOf(0, 20, 0)))
    check(productOfOthers(intArrayOf(3, 0, 2, 0)).contentEquals(intArrayOf(0, 0, 0, 0)))
    check(productOfOthers(intArrayOf(-1, 2, -3)).contentEquals(intArrayOf(-6, 3, -2)))
    check(productOfOthers(intArrayOf(7)).contentEquals(intArrayOf(1)))
    check(productOfOthers(intArrayOf()).isEmpty())
    println("016 ok")
}
