// 020 · Zero Out Rows and Columns
package p020

fun zeroOut(grid: Array<IntArray>) {
    val zeroRow = BooleanArray(grid.size)
    val zeroCol = BooleanArray(grid[0].size)
    for (r in grid.indices) {
        for (c in grid[0].indices) {
            if (grid[r][c] == 0) {
                zeroRow[r] = true
                zeroCol[c] = true
            }
        }
    }
    for (r in grid.indices) {
        for (c in grid[0].indices) {
            if (zeroRow[r] || zeroCol[c]) grid[r][c] = 0
        }
    }
}

fun zeroed(vararg rows: IntArray): Array<IntArray> = arrayOf(*rows).also { zeroOut(it) }

fun main() {
    check(zeroed(intArrayOf(1, 2, 3), intArrayOf(4, 0, 6), intArrayOf(7, 8, 9))
        .contentDeepEquals(arrayOf(intArrayOf(1, 0, 3), intArrayOf(0, 0, 0), intArrayOf(7, 0, 9))))
    check(zeroed(intArrayOf(0, 1, 2, 0), intArrayOf(3, 4, 5, 6), intArrayOf(1, 3, 1, 5))
        .contentDeepEquals(arrayOf(intArrayOf(0, 0, 0, 0), intArrayOf(0, 4, 5, 0), intArrayOf(0, 3, 1, 0))))
    check(zeroed(intArrayOf(0, 1), intArrayOf(1, 1)).contentDeepEquals(arrayOf(intArrayOf(0, 0), intArrayOf(0, 1))))
    check(zeroed(intArrayOf(1, 2), intArrayOf(3, 4)).contentDeepEquals(arrayOf(intArrayOf(1, 2), intArrayOf(3, 4))))
    check(zeroed(intArrayOf(0)).contentDeepEquals(arrayOf(intArrayOf(0))))
    check(zeroed(intArrayOf(5, 0, 7)).contentDeepEquals(arrayOf(intArrayOf(0, 0, 0))))
    println("020 ok")
}
