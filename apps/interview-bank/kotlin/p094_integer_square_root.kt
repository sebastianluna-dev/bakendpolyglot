// 094 · Integer Square Root
package p094

fun integerSqrt(x: Int): Int {
    // Long, because mid * mid and x + 1 overflow Int near Int.MAX_VALUE
    var lo = 0L
    var hi = x.toLong() + 1 // looking for the first k with k * k > x
    while (lo < hi) {
        val mid = lo + (hi - lo) / 2
        if (mid * mid > x) hi = mid else lo = mid + 1
    }
    return (lo - 1).toInt()
}

fun main() {
    check(integerSqrt(17) == 4)
    check(integerSqrt(36) == 6)
    check(integerSqrt(0) == 0)
    check(integerSqrt(1) == 1)
    check(integerSqrt(2) == 1)
    check(integerSqrt(4) == 2)
    check(integerSqrt(2_147_395_599) == 46_339)
    check(integerSqrt(2_147_395_600) == 46_340)
    check(integerSqrt(Int.MAX_VALUE) == 46_340)
    println("094 ok")
}
