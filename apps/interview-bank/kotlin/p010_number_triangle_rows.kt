// 010 · Number Triangle Rows
package p010

fun triangleRows(n: Int): List<List<Int>> {
    val rows = mutableListOf<List<Int>>()
    for (r in 0 until n) {
        val row = MutableList(r + 1) { 1 }
        for (c in 1 until r) row[c] = rows[r - 1][c - 1] + rows[r - 1][c]
        rows.add(row)
    }
    return rows
}

fun main() {
    check(triangleRows(5) == listOf(listOf(1), listOf(1, 1), listOf(1, 2, 1), listOf(1, 3, 3, 1), listOf(1, 4, 6, 4, 1)))
    check(triangleRows(2) == listOf(listOf(1), listOf(1, 1)))
    check(triangleRows(1) == listOf(listOf(1)))
    check(triangleRows(0).isEmpty())
    check(triangleRows(30)[29][14] == 77558760)
    println("010 ok")
}
