// 161 · Place n Queens
package p161

fun placeQueens(n: Int): List<List<String>> {
    val result = mutableListOf<List<String>>()
    val queens = mutableListOf<Int>() // queens[r] is the column of the queen in row r
    val cols = BooleanArray(n)
    val downs = BooleanArray(2 * n - 1) // r + c is the same along a down-left diagonal
    val ups = BooleanArray(2 * n - 1) // r - c + n - 1 is the same along a down-right diagonal
    fun walk(r: Int) {
        if (r == n) {
            result.add(queens.map { c -> ".".repeat(c) + "Q" + ".".repeat(n - c - 1) })
            return
        }
        for (c in 0 until n) {
            if (cols[c] || downs[r + c] || ups[r - c + n - 1]) continue
            cols[c] = true
            downs[r + c] = true
            ups[r - c + n - 1] = true
            queens.add(c)
            walk(r + 1)
            queens.removeAt(queens.lastIndex)
            cols[c] = false
            downs[r + c] = false
            ups[r - c + n - 1] = false
        }
    }
    walk(0)
    return result
}

fun main() {
    check(placeQueens(4) == listOf(listOf(".Q..", "...Q", "Q...", "..Q."), listOf("..Q.", "Q...", "...Q", ".Q..")))
    check(placeQueens(1) == listOf(listOf("Q")))
    check(placeQueens(2).isEmpty())
    check(placeQueens(3).isEmpty())
    check(placeQueens(6).size == 4)
    val eight = placeQueens(8)
    check(eight.size == 92)
    check(eight[0] == listOf("Q.......", "....Q...", ".......Q", ".....Q..", "..Q.....", "......Q.", ".Q......", "...Q...."))
    println("161 ok")
}
