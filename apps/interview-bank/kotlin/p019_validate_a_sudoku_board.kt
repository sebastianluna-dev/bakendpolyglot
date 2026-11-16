// 019 · Validate a Sudoku Board
package p019

fun isValidSudoku(board: List<String>): Boolean {
    val inRow = Array(9) { BooleanArray(9) }
    val inCol = Array(9) { BooleanArray(9) }
    val inBox = Array(9) { BooleanArray(9) }
    for (r in 0 until 9) {
        for (c in 0 until 9) {
            val ch = board[r][c]
            if (ch == '.') continue
            val d = ch - '1'
            val b = r / 3 * 3 + c / 3
            if (inRow[r][d] || inCol[c][d] || inBox[b][d]) return false
            inRow[r][d] = true
            inCol[c][d] = true
            inBox[b][d] = true
        }
    }
    return true
}

fun main() {
    val board = listOf(".9.5.3.46", "5.3.46...", "1........", "9.5.....2", "..1.6...5", "4..9....1", ".583...2.", "3.46.9758", ".2...8.1.")
    fun change(r: Int, row: String) = board.toMutableList().also { it[r] = row }
    check(isValidSudoku(board))
    check(!isValidSudoku(change(2, "1.9......"))) // box only
    check(!isValidSudoku(change(0, "69.5.3.46"))) // row only
    check(!isValidSudoku(change(4, "3.1.6...5"))) // column only
    check(isValidSudoku(List(9) { "........." }))
    println("019 ok")
}
