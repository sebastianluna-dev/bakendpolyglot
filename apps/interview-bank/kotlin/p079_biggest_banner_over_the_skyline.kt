// 079 · Biggest Banner Over the Skyline
package p079

fun biggestBanner(heights: IntArray): Int {
    var best = 0
    val stack = ArrayDeque<Int>() // indices of rising heights
    for (i in 0..heights.size) {
        val h = if (i < heights.size) heights[i] else 0 // a zero at the end empties the stack
        while (stack.isNotEmpty() && heights[stack.last()] >= h) {
            val top = stack.removeLast()
            val left = stack.lastOrNull() ?: -1
            best = maxOf(best, heights[top] * (i - left - 1))
        }
        stack.addLast(i)
    }
    return best
}

fun main() {
    check(biggestBanner(intArrayOf(3, 1, 4, 5, 4, 2)) == 12)
    check(biggestBanner(intArrayOf(2, 4, 2)) == 6)
    check(biggestBanner(intArrayOf()) == 0)
    check(biggestBanner(intArrayOf(7)) == 7)
    check(biggestBanner(intArrayOf(1, 2, 3, 4, 5)) == 9)
    check(biggestBanner(intArrayOf(5, 4, 3, 2, 1)) == 9)
    check(biggestBanner(intArrayOf(2, 1, 2)) == 3)
    check(biggestBanner(intArrayOf(0, 0)) == 0)
    check(biggestBanner(intArrayOf(4, 0, 4)) == 4)
    println("079 ok")
}
