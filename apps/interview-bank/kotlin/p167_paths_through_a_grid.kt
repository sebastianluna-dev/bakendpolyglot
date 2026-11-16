// 167 · Paths Through a Grid
package p167

fun gridPaths(rows: Int, cols: Int): Int {
    val row = IntArray(cols) { 1 } // the top row: one path to each cell
    for (r in 1 until rows) {
        for (c in 1 until cols) {
            row[c] += row[c - 1] // from above (old row[c]) plus from the left
        }
    }
    return row[cols - 1]
}

fun main() {
    check(gridPaths(3, 4) == 10)
    check(gridPaths(2, 2) == 2)
    check(gridPaths(1, 1) == 1)
    check(gridPaths(1, 5) == 1)
    check(gridPaths(5, 1) == 1)
    check(gridPaths(3, 7) == 28)
    check(gridPaths(10, 10) == 48620)
    check(gridPaths(17, 17) == 601080390)
    println("167 ok")
}
