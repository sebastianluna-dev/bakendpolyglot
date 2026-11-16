// 127 · Perimeter of an Island
package p127

fun islandPerimeter(grid: Array<IntArray>): Int {
    var perimeter = 0
    for (r in grid.indices) {
        for (c in grid[r].indices) {
            if (grid[r][c] != 1) continue
            perimeter += 4
            if (r > 0 && grid[r - 1][c] == 1) perimeter -= 2 // the shared side is lost by both cells
            if (c > 0 && grid[r][c - 1] == 1) perimeter -= 2
        }
    }
    return perimeter
}

fun main() {
    check(islandPerimeter(arrayOf(intArrayOf(0, 1, 0), intArrayOf(1, 1, 1), intArrayOf(0, 1, 0))) == 12)
    check(islandPerimeter(arrayOf(intArrayOf(1, 1), intArrayOf(1, 1))) == 8)
    check(islandPerimeter(arrayOf(intArrayOf(1))) == 4)
    check(islandPerimeter(arrayOf(intArrayOf(0, 0, 0), intArrayOf(0, 1, 0))) == 4)
    check(islandPerimeter(arrayOf(intArrayOf(1, 1, 1, 0))) == 8)
    check(islandPerimeter(arrayOf(intArrayOf(1, 1, 1), intArrayOf(1, 0, 1), intArrayOf(1, 1, 1))) == 16)
    println("127 ok")
}
