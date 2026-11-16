// 099 · Search a Sorted Matrix
package p099

fun searchSortedMatrix(shelves: Array<IntArray>, target: Int): Boolean {
    val rows = shelves.size
    val cols = if (rows == 0) 0 else shelves[0].size
    val total = rows * cols
    var lo = 0
    var hi = total // positions 0 .. total - 1, read row by row as one sorted array
    while (lo < hi) {
        val mid = lo + (hi - lo) / 2
        if (shelves[mid / cols][mid % cols] >= target) hi = mid else lo = mid + 1
    }
    return lo < total && shelves[lo / cols][lo % cols] == target
}

fun main() {
    val branch = arrayOf(
        intArrayOf(1, 4, 7, 11),
        intArrayOf(15, 18, 21, 26),
        intArrayOf(30, 34, 40, 52),
    )
    val column = arrayOf(intArrayOf(1), intArrayOf(3), intArrayOf(5))
    check(searchSortedMatrix(branch, 21))
    check(!searchSortedMatrix(branch, 13))
    check(!searchSortedMatrix(arrayOf(), 1))
    check(!searchSortedMatrix(arrayOf(intArrayOf()), 1))
    check(searchSortedMatrix(arrayOf(intArrayOf(5)), 5))
    check(!searchSortedMatrix(arrayOf(intArrayOf(5)), 6))
    check(!searchSortedMatrix(branch, 60))
    check(!searchSortedMatrix(branch, 0))
    check(searchSortedMatrix(branch, 1))
    check(searchSortedMatrix(branch, 52))
    check(searchSortedMatrix(branch, 26))
    check(searchSortedMatrix(branch, 30))
    check(searchSortedMatrix(column, 3))
    check(!searchSortedMatrix(column, 4))
    println("099 ok")
}
