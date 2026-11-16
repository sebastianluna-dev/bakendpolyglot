// 176 · Largest Product of a Stretch
package p176

fun largestProduct(nums: IntArray): Int {
    var best = nums[0]
    var hi = nums[0] // largest product of a stretch that ends at the current element
    var lo = nums[0] // smallest one: a negative number turns it into the largest
    for (i in 1 until nums.size) {
        val x = nums[i]
        val a = hi * x
        val b = lo * x
        hi = maxOf(x, a, b)
        lo = minOf(x, a, b)
        best = maxOf(best, hi)
    }
    return best
}

fun main() {
    check(largestProduct(intArrayOf(2, -5, -2, 4, 0, 3)) == 80)
    check(largestProduct(intArrayOf(-3, 0, -2)) == 0)
    check(largestProduct(intArrayOf(-2)) == -2)
    check(largestProduct(intArrayOf(-2, 3, -4)) == 24)
    check(largestProduct(intArrayOf(0, 0)) == 0)
    check(largestProduct(intArrayOf(-1, -1, -1)) == 1)
    check(largestProduct(intArrayOf(3, -1, 4)) == 4)
    check(largestProduct(intArrayOf(-4, 0, -5, 0, -6, 2)) == 2)
    check(largestProduct(IntArray(9) { 10 }) == 1000000000)
    println("176 ok")
}
