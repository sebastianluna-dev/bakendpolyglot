// 162 · Solve a Sudoku
package p162

fun solveSudoku(board: List<String>): List<String> {
    val grid = Array(9) { board[it].toCharArray() }
    val inRow = Array(9) { BooleanArray(10) }
    val inCol = Array(9) { BooleanArray(10) }
    val inBox = Array(9) { BooleanArray(10) }
    val empty = mutableListOf<Pair<Int, Int>>()
    for (r in 0 until 9) {
        for (c in 0 until 9) {
            if (grid[r][c] == '.') {
                empty.add(r to c)
            } else {
                val d = grid[r][c] - '0'
                inRow[r][d] = true
                inCol[c][d] = true
                inBox[r / 3 * 3 + c / 3][d] = true
            }
        }
    }
    fun fill(k: Int): Boolean {
        if (k == empty.size) return true
        val (r, c) = empty[k]
        val b = r / 3 * 3 + c / 3
        for (d in 1..9) {
            if (inRow[r][d] || inCol[c][d] || inBox[b][d]) continue
            inRow[r][d] = true
            inCol[c][d] = true
            inBox[b][d] = true
            grid[r][c] = '0' + d
            if (fill(k + 1)) return true // keep the digits: the board is solved
            inRow[r][d] = false
            inCol[c][d] = false
            inBox[b][d] = false
            grid[r][c] = '.'
        }
        return false
    }
    return if (fill(0)) grid.map { String(it) } else emptyList()
}

fun main() {
    val puzzle = listOf("..59..386", ".86....29", "1....3...", ".51...8..", "8.4..7...", "...46...1", ".12....4.", "..87..5..", "647.....8")
    val solved = listOf("475921386", "386574129", "129683475", "751392864", "864157293", "293468751", "512839647", "938746512", "647215938")
    check(solveSudoku(puzzle) == solved)
    val stuck = listOf("12345678.", "........9") + List(7) { "........." }
    check(solveSudoku(stuck).isEmpty())
    check(solveSudoku(solved) == solved)
    val easy = listOf(".1.52.3..", "2.57..1.6", "43...1...", "39.21..48", "......5.1", "15...7963", "76..9.48.", "5.8.7.21.", "..1...637")
    val easySolved = listOf("619528374", "285743196", "437961852", "396215748", "874639521", "152487963", "763192485", "548376219", "921854637")
    check(solveSudoku(easy) == easySolved)
    println("162 ok")
}
