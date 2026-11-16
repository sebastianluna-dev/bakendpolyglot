// 050 · Squares of a Sorted Array
package p050

import kotlin.math.abs

fun sortedSquares(nums: IntArray): IntArray {
    val out = IntArray(nums.size)
    var lo = 0
    var hi = nums.size - 1
    for (k in nums.indices.reversed()) {
        // the largest square left is at one of the two ends
        if (abs(nums[lo]) > abs(nums[hi])) {
            out[k] = nums[lo] * nums[lo]
            lo++
        } else {
            out[k] = nums[hi] * nums[hi]
            hi--
        }
    }
    return out
}

fun main() {
    check(sortedSquares(intArrayOf(-5, -2, 0, 3, 4)).contentEquals(intArrayOf(0, 4, 9, 16, 25)))
    check(sortedSquares(intArrayOf(-3, -3, -1)).contentEquals(intArrayOf(1, 9, 9)))
    check(sortedSquares(intArrayOf()).isEmpty())
    check(sortedSquares(intArrayOf(2, 5)).contentEquals(intArrayOf(4, 25)))
    check(sortedSquares(intArrayOf(-7)).contentEquals(intArrayOf(49)))
    check(sortedSquares(intArrayOf(-4, -1, 1, 4)).contentEquals(intArrayOf(1, 1, 16, 16)))
    println("050 ok")
}
