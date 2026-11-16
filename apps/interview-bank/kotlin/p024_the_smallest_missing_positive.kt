// 024 · The Smallest Missing Positive
package p024

fun smallestMissingPositive(nums: IntArray): Int {
    val n = nums.size
    for (i in 0 until n) {
        // keep swapping until slot i holds a value that has no slot or is already in its slot
        while (nums[i] in 1..n && nums[nums[i] - 1] != nums[i]) {
            val j = nums[i] - 1
            val tmp = nums[i]
            nums[i] = nums[j]
            nums[j] = tmp
        }
    }
    for (i in 0 until n) {
        if (nums[i] != i + 1) return i + 1
    }
    return n + 1
}

fun main() {
    check(smallestMissingPositive(intArrayOf(3, 1, -2, 2, 7)) == 4)
    check(smallestMissingPositive(intArrayOf(5, 2, 2, 9)) == 1)
    check(smallestMissingPositive(intArrayOf()) == 1)
    check(smallestMissingPositive(intArrayOf(1, 2, 3)) == 4)
    check(smallestMissingPositive(intArrayOf(0, -1, -5)) == 1)
    check(smallestMissingPositive(intArrayOf(1, 1)) == 2)
    check(smallestMissingPositive(intArrayOf(2, 3, 4, 1)) == 5)
    check(smallestMissingPositive(intArrayOf(2147483647, 1)) == 2)
    println("024 ok")
}
