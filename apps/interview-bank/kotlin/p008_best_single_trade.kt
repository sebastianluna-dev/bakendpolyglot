// 008 · Best Single Trade
package p008

fun bestTrade(prices: IntArray): Int {
    var lowest = Int.MAX_VALUE
    var best = 0
    for (price in prices) {
        lowest = minOf(lowest, price)
        best = maxOf(best, price - lowest)
    }
    return best
}

fun main() {
    check(bestTrade(intArrayOf(8, 3, 6, 1, 7, 4)) == 6)
    check(bestTrade(intArrayOf(9, 7, 4, 2)) == 0)
    check(bestTrade(intArrayOf(5, 9, 1)) == 4)
    check(bestTrade(intArrayOf(3)) == 0)
    check(bestTrade(intArrayOf()) == 0)
    println("008 ok")
}
