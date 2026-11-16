// 132 · Water That Reaches Both Coasts
package p132

private val DIRS = listOf(1 to 0, -1 to 0, 0 to 1, 0 to -1)

fun bothCoasts(heights: Array<IntArray>): List<Pair<Int, Int>> {
    val rows = heights.size
    val cols = heights[0].size
    val northWestEdge = mutableListOf<Pair<Int, Int>>()
    val southEastEdge = mutableListOf<Pair<Int, Int>>()
    for (r in 0..<rows) {
        for (c in 0..<cols) {
            if (r == 0 || c == 0) northWestEdge.add(r to c)
            if (r == rows - 1 || c == cols - 1) southEastEdge.add(r to c)
        }
    }
    val northWest = reach(heights, northWestEdge)
    val southEast = reach(heights, southEastEdge)
    val cells = mutableListOf<Pair<Int, Int>>()
    for (r in 0..<rows) {
        for (c in 0..<cols) {
            if (northWest[r][c] && southEast[r][c]) cells.add(r to c)
        }
    }
    return cells
}

fun reach(heights: Array<IntArray>, starts: List<Pair<Int, Int>>): Array<BooleanArray> {
    val rows = heights.size
    val cols = heights[0].size
    val seen = Array(rows) { BooleanArray(cols) }
    for ((r, c) in starts) seen[r][c] = true
    val queue = ArrayDeque(starts)
    while (queue.isNotEmpty()) {
        val (r, c) = queue.removeFirst()
        for ((dr, dc) in DIRS) {
            val nr = r + dr
            val nc = c + dc
            // uphill or level: water from (nr, nc) could flow down to (r, c)
            if (nr !in 0..<rows || nc !in 0..<cols || seen[nr][nc] || heights[nr][nc] < heights[r][c]) continue
            seen[nr][nc] = true
            queue.addLast(nr to nc)
        }
    }
    return seen
}

fun main() {
    check(bothCoasts(arrayOf(intArrayOf(1, 2, 2, 3), intArrayOf(3, 2, 3, 4), intArrayOf(2, 4, 5, 3), intArrayOf(6, 7, 1, 4))) ==
        listOf(0 to 3, 1 to 3, 2 to 2, 3 to 0, 3 to 1))
    check(bothCoasts(arrayOf(intArrayOf(5, 5, 5), intArrayOf(5, 1, 5), intArrayOf(5, 5, 5))) ==
        listOf(0 to 0, 0 to 1, 0 to 2, 1 to 0, 1 to 2, 2 to 0, 2 to 1, 2 to 2))
    check(bothCoasts(arrayOf(intArrayOf(4, 3, 2), intArrayOf(5, 1, 3), intArrayOf(6, 2, 1))) ==
        listOf(0 to 0, 0 to 1, 0 to 2, 1 to 0, 1 to 2, 2 to 0))
    check(bothCoasts(arrayOf(intArrayOf(1, 2, 3))) == listOf(0 to 0, 0 to 1, 0 to 2))
    check(bothCoasts(arrayOf(intArrayOf(7))) == listOf(0 to 0))
    println("132 ok")
}
