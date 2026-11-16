// 151 · Fast Power
package p151

fun powerMod(x: Long, n: Long, m: Long): Long {
    if (n == 0L) return 1 % m
    val half = powerMod(x, n / 2, m) // one recursive call, used twice
    val square = half * half % m
    return if (n % 2 == 0L) square else square * (x % m) % m
}

fun main() {
    check(powerMod(3, 13, 100) == 23L)
    check(powerMod(7, 0, 13) == 1L)
    check(powerMod(2, 10, 1000) == 24L)
    check(powerMod(5, 3, 1) == 0L)
    check(powerMod(0, 0, 7) == 1L)
    check(powerMod(0, 5, 7) == 0L)
    check(powerMod(999_999_999, 2, 1_000_000_007) == 64L)
    check(powerMod(10, 9, 1_000_000_007) == 1_000_000_000L)
    check(powerMod(2, 1_000_000_006, 1_000_000_007) == 1L)
    println("151 ok")
}
