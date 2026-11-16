// 131 · Minutes Until the Outage Spreads
package p131

private val DIRS = listOf(1 to 0, -1 to 0, 0 to 1, 0 to -1)

fun minutesUntilAllFail(grid: Array<IntArray>): Int {
    val rows = grid.size
    val cols = grid[0].size
    val queue = ArrayDeque<Pair<Int, Int>>()
    var healthy = 0
    for (r in 0..<rows) {
        for (c in 0..<cols) {
            if (grid[r][c] == 2) queue.addLast(r to c)
            else if (grid[r][c] == 1) healthy++
        }
    }
    var minutes = 0
    while (queue.isNotEmpty() && healthy > 0) {
        minutes++
        repeat(queue.size) { // one minute: only the servers that failed last minute
            val (r, c) = queue.removeFirst()
            for ((dr, dc) in DIRS) {
                val nr = r + dr
                val nc = c + dc
                if (nr !in 0..<rows || nc !in 0..<cols || grid[nr][nc] != 1) continue
                grid[nr][nc] = 2
                healthy--
                queue.addLast(nr to nc)
            }
        }
    }
    return if (healthy == 0) minutes else -1
}

fun main() {
    check(minutesUntilAllFail(arrayOf(intArrayOf(1, 1, 0, 2), intArrayOf(0, 1, 1, 1), intArrayOf(0, 0, 0, 1))) == 5)
    check(minutesUntilAllFail(arrayOf(intArrayOf(1, 1, 0, 2), intArrayOf(0, 1, 1, 1), intArrayOf(1, 0, 0, 1))) == -1)
    check(minutesUntilAllFail(arrayOf(intArrayOf(0, 2))) == 0)
    check(minutesUntilAllFail(arrayOf(intArrayOf(0))) == 0)
    check(minutesUntilAllFail(arrayOf(intArrayOf(1))) == -1)
    check(minutesUntilAllFail(arrayOf(intArrayOf(2, 1))) == 1)
    check(minutesUntilAllFail(arrayOf(intArrayOf(2, 1, 1, 1, 2))) == 2)
    println("131 ok")
}
