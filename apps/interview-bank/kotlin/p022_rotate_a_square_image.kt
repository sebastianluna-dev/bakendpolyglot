// 022 · Rotate a Square Image
package p022

fun rotateImage(image: Array<IntArray>) {
    val n = image.size
    for (i in 0 until n) {
        for (j in i + 1 until n) {
            val tmp = image[i][j]
            image[i][j] = image[j][i]
            image[j][i] = tmp
        }
    }
    for (row in image) row.reverse()
}

fun rotated(image: Array<IntArray>): Array<IntArray> {
    rotateImage(image)
    return image
}

fun grid(vararg rows: IntArray): Array<IntArray> = arrayOf(*rows)

fun main() {
    check(rotated(grid(intArrayOf(1, 2), intArrayOf(3, 4))).contentDeepEquals(grid(intArrayOf(3, 1), intArrayOf(4, 2))))
    check(
        rotated(grid(intArrayOf(11, 12, 13), intArrayOf(21, 22, 23), intArrayOf(31, 32, 33)))
            .contentDeepEquals(grid(intArrayOf(31, 21, 11), intArrayOf(32, 22, 12), intArrayOf(33, 23, 13))),
    )
    check(rotated(grid()).isEmpty())
    check(rotated(grid(intArrayOf(7))).contentDeepEquals(grid(intArrayOf(7))))
    check(
        rotated(grid(intArrayOf(1, 2, 3, 4), intArrayOf(5, 6, 7, 8), intArrayOf(9, 10, 11, 12), intArrayOf(13, 14, 15, 16)))
            .contentDeepEquals(grid(intArrayOf(13, 9, 5, 1), intArrayOf(14, 10, 6, 2), intArrayOf(15, 11, 7, 3), intArrayOf(16, 12, 8, 4))),
    )
    println("022 ok")
}
