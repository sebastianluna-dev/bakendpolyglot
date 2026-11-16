// 169 · Region Totals on a Seat Map
package p169

class RegionSums(grid: Array<IntArray>) {
    // prefix[r][c] = total of the rows above r and the columns left of c
    private val prefix = Array(grid.size + 1) { IntArray(grid[0].size + 1) }

    init {
        for (r in grid.indices) {
            for (c in grid[0].indices) {
                prefix[r + 1][c + 1] = grid[r][c] + prefix[r][c + 1] + prefix[r + 1][c] - prefix[r][c]
            }
        }
    }

    fun sum(r1: Int, c1: Int, r2: Int, c2: Int): Int =
        prefix[r2 + 1][c2 + 1] - prefix[r1][c2 + 1] - prefix[r2 + 1][c1] + prefix[r1][c1]
}

fun main() {
    val seats = RegionSums(
        arrayOf(
            intArrayOf(3, 0, 1, 4),
            intArrayOf(5, 6, 3, 2),
            intArrayOf(1, 2, 0, 1),
            intArrayOf(4, 1, 0, 5),
        ),
    )
    check(seats.sum(1, 1, 2, 2) == 11)
    check(seats.sum(0, 0, 3, 3) == 38)
    check(seats.sum(2, 3, 2, 3) == 1)
    check(seats.sum(0, 1, 3, 2) == 13)
    check(seats.sum(1, 0, 1, 3) == 16)
    check(seats.sum(2, 2, 3, 3) == 6)
    check(RegionSums(arrayOf(intArrayOf(7))).sum(0, 0, 0, 0) == 7)
    println("169 ok")
}
