// 093 · The First Failing Build
package p093

fun firstFailingBuild(n: Int, isBad: (Int) -> Boolean): Int {
    var lo = 1
    var hi = n + 1 // n + 1 stands for "no build fails"
    while (lo < hi) {
        val mid = lo + (hi - lo) / 2
        if (isBad(mid)) hi = mid else lo = mid + 1
    }
    return if (lo <= n) lo else -1
}

var calls = 0

fun failingFrom(first: Int): (Int) -> Boolean = { build ->
    calls++
    build >= first
}

fun main() {
    check(firstFailingBuild(5, failingFrom(4)) == 4)
    check(firstFailingBuild(3, failingFrom(4)) == -1)
    check(firstFailingBuild(1, failingFrom(1)) == 1)
    check(firstFailingBuild(1, failingFrom(2)) == -1)
    check(firstFailingBuild(5, failingFrom(1)) == 1)
    calls = 0
    check(firstFailingBuild(1000, failingFrom(777)) == 777)
    check(calls <= 10)
    calls = 0
    check(firstFailingBuild(2_000_000_000, failingFrom(1_999_999_999)) == 1_999_999_999)
    check(calls <= 31)
    println("093 ok")
}
