// 101 · Median of Two Sorted Arrays
package p101

fun medianOfTwoSorted(a: IntArray, b: IntArray): Double {
    if (a.size > b.size) return medianOfTwoSorted(b, a)
    val m = a.size
    val n = b.size
    val half = (m + n + 1) / 2
    var lo = 0
    var hi = m // m stands for "take all of a"
    while (lo < hi) {
        val mid = lo + (hi - lo) / 2 // values of a in the left half
        val rest = half - mid
        if (rest == 0 || b[rest - 1] <= a[mid]) hi = mid else lo = mid + 1
    }
    val i = lo
    val j = half - i
    val leftMax = maxOf(if (i > 0) a[i - 1] else Int.MIN_VALUE, if (j > 0) b[j - 1] else Int.MIN_VALUE)
    if ((m + n) % 2 == 1) return leftMax.toDouble()
    val rightMin = minOf(if (i < m) a[i] else Int.MAX_VALUE, if (j < n) b[j] else Int.MAX_VALUE)
    return (leftMax + rightMin) / 2.0
}

fun main() {
    check(medianOfTwoSorted(intArrayOf(12, 18, 31), intArrayOf(9, 20, 25, 40)) == 20.0)
    check(medianOfTwoSorted(intArrayOf(5, 9), intArrayOf(6, 30)) == 7.5)
    check(medianOfTwoSorted(intArrayOf(), intArrayOf(4)) == 4.0)
    check(medianOfTwoSorted(intArrayOf(1, 2), intArrayOf()) == 1.5)
    check(medianOfTwoSorted(intArrayOf(2, 2, 2), intArrayOf(2, 2)) == 2.0)
    check(medianOfTwoSorted(intArrayOf(1, 2), intArrayOf(3, 4, 5, 6)) == 3.5)
    check(medianOfTwoSorted(intArrayOf(1, 3, 5, 7, 9), intArrayOf(2)) == 4.0)
    check(medianOfTwoSorted(intArrayOf(-5, -1), intArrayOf(-3)) == -3.0)
    check(medianOfTwoSorted(intArrayOf(10), intArrayOf(1, 2, 3)) == 2.5)
    println("101 ok")
}
