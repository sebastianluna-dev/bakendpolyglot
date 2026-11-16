// 047 · Merge Two Sorted Arrays in Place
package p047

fun mergeInPlace(a: IntArray, m: Int, b: IntArray) {
    var i = m - 1
    var j = b.size - 1
    var k = m + b.size - 1 // next slot to fill, from the back
    while (j >= 0) {
        if (i >= 0 && a[i] > b[j]) a[k--] = a[i--]
        else a[k--] = b[j--]
    }
}

fun merged(a: IntArray, m: Int, b: IntArray): IntArray = a.also { mergeInPlace(it, m, b) }

fun main() {
    check(merged(intArrayOf(1, 4, 7, 0, 0), 3, intArrayOf(2, 8)).contentEquals(intArrayOf(1, 2, 4, 7, 8)))
    check(merged(intArrayOf(0, 0), 0, intArrayOf(3, 5)).contentEquals(intArrayOf(3, 5)))
    check(merged(intArrayOf(2, 3), 2, intArrayOf()).contentEquals(intArrayOf(2, 3)))
    check(merged(intArrayOf(1, 2, 2, 0, 0, 0), 3, intArrayOf(2, 2, 3)).contentEquals(intArrayOf(1, 2, 2, 2, 2, 3)))
    check(merged(intArrayOf(-3, 5, 0), 2, intArrayOf(-4)).contentEquals(intArrayOf(-4, -3, 5)))
    check(merged(intArrayOf(4, 5, 0), 2, intArrayOf(1)).contentEquals(intArrayOf(1, 4, 5)))
    println("047 ok")
}
