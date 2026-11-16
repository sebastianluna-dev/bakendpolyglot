// 064 · Rain Caught Between Walls
package p064

fun trappedRain(heights: IntArray): Int {
    var left = 0
    var right = heights.size - 1
    var leftMax = 0
    var rightMax = 0
    var water = 0
    while (left < right) {
        // The lower end decides: the far side has a wall at least this high.
        if (heights[left] < heights[right]) {
            leftMax = maxOf(leftMax, heights[left])
            water += leftMax - heights[left]
            left++
        } else {
            rightMax = maxOf(rightMax, heights[right])
            water += rightMax - heights[right]
            right--
        }
    }
    return water
}

fun main() {
    check(trappedRain(intArrayOf(3, 0, 2, 0, 4)) == 7)
    check(trappedRain(intArrayOf(5, 4, 3)) == 0)
    check(trappedRain(intArrayOf()) == 0)
    check(trappedRain(intArrayOf(7)) == 0)
    check(trappedRain(intArrayOf(2, 0, 2)) == 2)
    check(trappedRain(intArrayOf(0, 2, 0, 1, 0, 3, 1, 0, 2)) == 8)
    check(trappedRain(intArrayOf(4, 1, 1, 4)) == 6)
    check(trappedRain(intArrayOf(3, 0, 1)) == 1)
    println("064 ok")
}
