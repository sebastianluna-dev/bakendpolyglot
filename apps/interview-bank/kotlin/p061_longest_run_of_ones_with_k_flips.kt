// 061 · Longest Run of Ones With k Flips
package p061

fun longestOnes(seats: IntArray, k: Int): Int {
    var left = 0
    var zeros = 0 // zeros inside seats[left..right]
    var best = 0
    for (right in seats.indices) {
        if (seats[right] == 0) zeros++
        while (zeros > k) {
            if (seats[left] == 0) zeros--
            left++
        }
        best = maxOf(best, right - left + 1)
    }
    return best
}

fun main() {
    check(longestOnes(intArrayOf(1, 1, 0, 1, 0, 0, 1, 1, 1, 0, 1), 2) == 6)
    check(longestOnes(intArrayOf(1, 0, 1, 1, 1, 0), 0) == 3)
    check(longestOnes(intArrayOf(), 3) == 0)
    check(longestOnes(intArrayOf(0, 0, 0), 0) == 0)
    check(longestOnes(intArrayOf(0, 0, 0), 5) == 3)
    check(longestOnes(intArrayOf(1, 0, 0, 1), 1) == 2)
    check(longestOnes(intArrayOf(1, 1, 1), 1) == 3)
    println("061 ok")
}
