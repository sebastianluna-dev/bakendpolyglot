// 128 · Count the Islands
package p128

private val DIRS = listOf(1 to 0, -1 to 0, 0 to 1, 0 to -1)

fun countIslands(grid: Array<IntArray>): Int {
    val rows = grid.size
    val cols = grid[0].size
    val seen = Array(rows) { BooleanArray(cols) }
    var islands = 0
    for (sr in 0..<rows) {
        for (sc in 0..<cols) {
            if (grid[sr][sc] != 1 || seen[sr][sc]) continue
            islands++
            seen[sr][sc] = true
            val queue = ArrayDeque<Pair<Int, Int>>()
            queue.addLast(sr to sc)
            while (queue.isNotEmpty()) {
                val (r, c) = queue.removeFirst()
                for ((dr, dc) in DIRS) {
                    val nr = r + dr
                    val nc = c + dc
                    if (nr !in 0..<rows || nc !in 0..<cols || grid[nr][nc] != 1 || seen[nr][nc]) continue
                    seen[nr][nc] = true
                    queue.addLast(nr to nc)
                }
            }
        }
    }
    return islands
}

fun main() {
    check(countIslands(arrayOf(intArrayOf(1, 1, 0, 0, 1), intArrayOf(1, 0, 0, 1, 1), intArrayOf(0, 0, 1, 0, 0), intArrayOf(1, 0, 0, 0, 1))) == 5)
    check(countIslands(arrayOf(intArrayOf(1, 0, 1), intArrayOf(0, 1, 0), intArrayOf(1, 0, 1))) == 5)
    check(countIslands(arrayOf(intArrayOf(0, 0), intArrayOf(0, 0))) == 0)
    check(countIslands(arrayOf(intArrayOf(1, 1), intArrayOf(1, 1))) == 1)
    check(countIslands(arrayOf(intArrayOf(1, 1, 1), intArrayOf(0, 0, 1), intArrayOf(1, 1, 1))) == 1)
    check(countIslands(arrayOf(intArrayOf(1))) == 1)
    println("128 ok")
}
