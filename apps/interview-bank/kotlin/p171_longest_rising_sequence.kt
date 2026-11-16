// 171 · Longest Rising Sequence
package p171

fun longestRising(nums: IntArray): Int {
    val tails = mutableListOf<Int>() // tails[k] = smallest last value of a rising sequence of length k + 1
    for (x in nums) {
        var lo = 0
        var hi = tails.size
        while (lo < hi) {
            // first tail >= x, so an equal value replaces instead of extending
            val mid = (lo + hi) / 2
            if (tails[mid] < x) lo = mid + 1 else hi = mid
        }
        if (lo == tails.size) tails.add(x) else tails[lo] = x
    }
    return tails.size
}

fun main() {
    check(longestRising(intArrayOf(3, 1, 8, 2, 5, 9, 4)) == 4)
    check(longestRising(intArrayOf(7, 7, 7)) == 1)
    check(longestRising(intArrayOf()) == 0)
    check(longestRising(intArrayOf(5)) == 1)
    check(longestRising(intArrayOf(9, 8, 7)) == 1)
    check(longestRising(intArrayOf(1, 2, 3, 4)) == 4)
    check(longestRising(intArrayOf(2, 2, 3, 3, 4)) == 3)
    check(longestRising(intArrayOf(6, 2, 9, 3, 7, 4, 10, 5, 8)) == 5)
    check(longestRising(intArrayOf(10, -3, 4, -1, 6, 2, 8)) == 4)
    println("171 ok")
}
