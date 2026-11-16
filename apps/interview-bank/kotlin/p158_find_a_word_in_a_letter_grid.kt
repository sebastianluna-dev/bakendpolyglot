// 158 · Find a Word in a Letter Grid
package p158

fun findWord(grid: List<String>, word: String): Boolean {
    val rows = grid.size
    val cols = grid[0].length
    val used = Array(rows) { BooleanArray(cols) }
    fun walk(r: Int, c: Int, k: Int): Boolean {
        if (k == word.length) return true
        if (r < 0 || r >= rows || c < 0 || c >= cols || used[r][c] || grid[r][c] != word[k]) return false
        used[r][c] = true
        val found = walk(r + 1, c, k + 1) || walk(r - 1, c, k + 1) || walk(r, c + 1, k + 1) || walk(r, c - 1, k + 1)
        used[r][c] = false // free the cell for paths that start elsewhere
        return found
    }
    for (r in 0 until rows) {
        for (c in 0 until cols) {
            if (walk(r, c, 0)) return true
        }
    }
    return false
}

fun main() {
    val grid = listOf("she", "afl", "nao")
    check(findWord(grid, "shelf"))
    check(!findWord(grid, "lol"))
    check(findWord(grid, "loan"))
    check(findWord(grid, "fan"))
    check(!findWord(grid, "sf"))
    check(findWord(grid, "o"))
    check(!findWord(grid, "z"))
    check(!findWord(grid, "shelfaloans"))
    check(findWord(listOf("aa", "ab"), "aaab"))
    println("158 ok")
}
