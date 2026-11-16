// 135 · Shortest Clear Path in a Grid
package p135

fun shortestClearPath(grid: Array<IntArray>): Int {
    val rows = grid.size
    val cols = grid[0].size
    if (grid[0][0] == 1 || grid[rows - 1][cols - 1] == 1) return -1
    val cells = Array(rows) { IntArray(cols) } // 0 = not reached
    cells[0][0] = 1
    val queue = ArrayDeque(listOf(0 to 0))
    while (queue.isNotEmpty()) {
        val (r, c) = queue.removeFirst()
        if (r == rows - 1 && c == cols - 1) return cells[r][c]
        for (dr in -1..1) {
            for (dc in -1..1) {
                val nr = r + dr
                val nc = c + dc
                if (nr !in 0 until rows || nc !in 0 until cols) continue
                if (grid[nr][nc] == 1 || cells[nr][nc] != 0) continue // also skips (r, c) itself
                cells[nr][nc] = cells[r][c] + 1
                queue.addLast(nr to nc)
            }
        }
    }
    return -1
}

fun main() {
    check(shortestClearPath(arrayOf(intArrayOf(0, 0, 0), intArrayOf(1, 1, 0), intArrayOf(1, 1, 0))) == 4)
    check(shortestClearPath(arrayOf(intArrayOf(0, 1), intArrayOf(1, 0))) == 2)
    check(shortestClearPath(arrayOf(intArrayOf(0))) == 1)
    check(shortestClearPath(arrayOf(intArrayOf(1))) == -1)
    check(shortestClearPath(arrayOf(intArrayOf(1, 0), intArrayOf(0, 0))) == -1)
    check(shortestClearPath(arrayOf(intArrayOf(0, 0), intArrayOf(0, 1))) == -1)
    check(shortestClearPath(arrayOf(intArrayOf(0, 1, 0), intArrayOf(1, 1, 0), intArrayOf(0, 0, 0))) == -1)
    check(shortestClearPath(arrayOf(intArrayOf(0, 0, 0, 0))) == 4)
    check(
        shortestClearPath(
            arrayOf(intArrayOf(0, 1, 0, 0), intArrayOf(0, 1, 0, 1), intArrayOf(0, 1, 0, 1), intArrayOf(1, 0, 1, 0)),
        ) == 6,
    )
    println("135 ok")
}
