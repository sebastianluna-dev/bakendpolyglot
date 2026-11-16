// 059 · Sort Three Colours in One Pass
package p059

fun sortColours(stickers: IntArray) {
    var low = 0 // [0, low) is 0
    var mid = 0 // [low, mid) is 1
    var high = stickers.size - 1 // (high, end] is 2
    while (mid <= high) {
        when (stickers[mid]) {
            0 -> {
                swap(stickers, low, mid)
                low++
                mid++
            }
            1 -> mid++
            else -> {
                swap(stickers, mid, high)
                high-- // mid stays: the value swapped in has not been looked at yet
            }
        }
    }
}

private fun swap(a: IntArray, i: Int, j: Int) {
    val t = a[i]
    a[i] = a[j]
    a[j] = t
}

private fun sorted(stickers: IntArray): IntArray {
    sortColours(stickers)
    return stickers
}

fun main() {
    check(sorted(intArrayOf(2, 0, 1, 2, 1, 0)).contentEquals(intArrayOf(0, 0, 1, 1, 2, 2)))
    check(sorted(intArrayOf(1, 1, 1)).contentEquals(intArrayOf(1, 1, 1)))
    check(sorted(intArrayOf()).isEmpty())
    check(sorted(intArrayOf(2, 0)).contentEquals(intArrayOf(0, 2)))
    check(sorted(intArrayOf(2, 2, 0, 0)).contentEquals(intArrayOf(0, 0, 2, 2)))
    check(sorted(intArrayOf(0, 2, 1)).contentEquals(intArrayOf(0, 1, 2)))
    check(sorted(intArrayOf(1, 2, 0)).contentEquals(intArrayOf(0, 1, 2)))
    check(sorted(intArrayOf(1, 2, 0, 2, 0, 1, 0)).contentEquals(intArrayOf(0, 0, 0, 1, 1, 2, 2)))
    println("059 ok")
}
