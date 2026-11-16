// 124 · Flood Fill
package p124

private val DIRS = listOf(1 to 0, -1 to 0, 0 to 1, 0 to -1)

fun floodFill(image: Array<IntArray>, row: Int, col: Int, color: Int): Array<IntArray> {
    val old = image[row][col]
    if (old == color) return image // nothing to do, and the loop below would never stop
    val rows = image.size
    val cols = image[0].size
    image[row][col] = color
    val queue = ArrayDeque<Pair<Int, Int>>()
    queue.addLast(row to col)
    while (queue.isNotEmpty()) {
        val (r, c) = queue.removeFirst()
        for ((dr, dc) in DIRS) {
            val nr = r + dr
            val nc = c + dc
            if (nr !in 0..<rows || nc !in 0..<cols || image[nr][nc] != old) continue
            image[nr][nc] = color
            queue.addLast(nr to nc)
        }
    }
    return image
}

fun main() {
    check(floodFill(arrayOf(intArrayOf(1, 1, 0), intArrayOf(0, 1, 0), intArrayOf(1, 0, 1)), 0, 0, 3)
        .contentDeepEquals(arrayOf(intArrayOf(3, 3, 0), intArrayOf(0, 3, 0), intArrayOf(1, 0, 1))))
    check(floodFill(arrayOf(intArrayOf(4, 4), intArrayOf(4, 4)), 1, 1, 4)
        .contentDeepEquals(arrayOf(intArrayOf(4, 4), intArrayOf(4, 4))))
    check(floodFill(arrayOf(intArrayOf(0)), 0, 0, 7).contentDeepEquals(arrayOf(intArrayOf(7))))
    check(floodFill(arrayOf(intArrayOf(1, 2, 1), intArrayOf(1, 2, 1), intArrayOf(1, 1, 1)), 0, 2, 9)
        .contentDeepEquals(arrayOf(intArrayOf(9, 2, 9), intArrayOf(9, 2, 9), intArrayOf(9, 9, 9))))
    check(floodFill(arrayOf(intArrayOf(5, 5, 6), intArrayOf(6, 5, 6)), 1, 2, 0)
        .contentDeepEquals(arrayOf(intArrayOf(5, 5, 0), intArrayOf(6, 5, 0))))
    println("124 ok")
}
