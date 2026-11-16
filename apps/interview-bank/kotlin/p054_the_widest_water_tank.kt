// 054 · The Widest Water Tank
package p054

fun widestTank(heights: IntArray): Int {
    var left = 0
    var right = heights.size - 1
    var best = 0
    while (left < right) {
        val area = (right - left) * minOf(heights[left], heights[right])
        best = maxOf(best, area)
        // The shorter wall limits every tank that keeps it, so move it inward.
        if (heights[left] <= heights[right]) left++ else right--
    }
    return best
}

fun main() {
    check(widestTank(intArrayOf(2, 6, 3, 1, 5, 2)) == 15)
    check(widestTank(intArrayOf(4, 4)) == 4)
    check(widestTank(intArrayOf(7)) == 0)
    check(widestTank(intArrayOf()) == 0)
    check(widestTank(intArrayOf(1, 2, 3, 4, 5)) == 6)
    check(widestTank(intArrayOf(0, 0, 0)) == 0)
    check(widestTank(intArrayOf(5, 1, 1, 1, 5)) == 20)
    println("054 ok")
}
