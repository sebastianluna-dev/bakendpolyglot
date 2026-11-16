// 021 · Read a Matrix in a Spiral
package p021

fun spiralOrder(grid: Array<IntArray>): List<Int> {
    val result = mutableListOf<Int>()
    if (grid.isEmpty()) return result
    var top = 0
    var bottom = grid.size - 1
    var left = 0
    var right = grid[0].size - 1
    while (top <= bottom && left <= right) {
        for (c in left..right) result.add(grid[top][c])
        top++
        for (r in top..bottom) result.add(grid[r][right])
        right--
        // after the two steps above, a single row or column may be used up
        if (top <= bottom) {
            for (c in right downTo left) result.add(grid[bottom][c])
            bottom--
        }
        if (left <= right) {
            for (r in bottom downTo top) result.add(grid[r][left])
            left++
        }
    }
    return result
}

fun main() {
    check(spiralOrder(arrayOf(intArrayOf(1, 2, 3), intArrayOf(8, 9, 4), intArrayOf(7, 6, 5))) == listOf(1, 2, 3, 4, 5, 6, 7, 8, 9))
    check(
        spiralOrder(arrayOf(intArrayOf(10, 11, 12, 13), intArrayOf(20, 21, 22, 23), intArrayOf(30, 31, 32, 33))) ==
            listOf(10, 11, 12, 13, 23, 33, 32, 31, 30, 20, 21, 22),
    )
    check(spiralOrder(arrayOf()).isEmpty())
    check(spiralOrder(arrayOf(intArrayOf(4, 5, 6))) == listOf(4, 5, 6))
    check(spiralOrder(arrayOf(intArrayOf(4), intArrayOf(5), intArrayOf(6))) == listOf(4, 5, 6))
    check(spiralOrder(arrayOf(intArrayOf(1, 2), intArrayOf(3, 4), intArrayOf(5, 6))) == listOf(1, 2, 4, 6, 5, 3))
    println("021 ok")
}
