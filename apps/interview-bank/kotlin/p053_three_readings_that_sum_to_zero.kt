// 053 · Three Readings That Sum to Zero
package p053

fun threeToZero(readings: IntArray): List<List<Int>> {
    val s = readings.sortedArray()
    val result = mutableListOf<List<Int>>()
    for (i in 0 until s.size - 2) {
        if (i > 0 && s[i] == s[i - 1]) continue // same first value, same triplets
        var lo = i + 1
        var hi = s.size - 1
        while (lo < hi) {
            val sum = s[i] + s[lo] + s[hi]
            if (sum < 0) {
                lo++
            } else if (sum > 0) {
                hi--
            } else {
                result.add(listOf(s[i], s[lo], s[hi]))
                lo++
                hi--
                while (lo < hi && s[lo] == s[lo - 1]) lo++
            }
        }
    }
    return result
}

fun main() {
    check(threeToZero(intArrayOf(-4, 1, 3, 0, -1, 2, -1)) == listOf(listOf(-4, 1, 3), listOf(-1, -1, 2), listOf(-1, 0, 1)))
    check(threeToZero(intArrayOf(0, 0, 0, 0)) == listOf(listOf(0, 0, 0)))
    check(threeToZero(intArrayOf(1, 2, -4)).isEmpty())
    check(threeToZero(intArrayOf()).isEmpty())
    check(threeToZero(intArrayOf(5, -5)).isEmpty())
    check(threeToZero(intArrayOf(2, -2, 0, 2, 0)) == listOf(listOf(-2, 0, 2)))
    println("053 ok")
}
