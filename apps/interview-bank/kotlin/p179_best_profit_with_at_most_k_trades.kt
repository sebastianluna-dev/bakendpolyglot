// 179 · Best Profit With at Most k Trades
package p179

fun bestProfit(prices: IntArray, k: Int): Int {
    if (prices.isEmpty() || k == 0) return 0
    val bought = IntArray(k + 1) { -prices[0] } // bought[t]: most cash holding the t-th share
    val sold = IntArray(k + 1) // sold[t]: most cash after t complete trades
    for (p in prices) {
        for (t in 1..k) {
            bought[t] = maxOf(bought[t], sold[t - 1] - p)
            sold[t] = maxOf(sold[t], bought[t] + p)
        }
    }
    return sold[k]
}

fun main() {
    val prices = intArrayOf(3, 8, 5, 1, 7, 8)
    val zigzag = intArrayOf(2, 5, 1, 4, 3, 6)
    check(bestProfit(prices, 2) == 12)
    check(bestProfit(prices, 1) == 7)
    check(bestProfit(intArrayOf(9, 7, 4, 1), 2) == 0)
    check(bestProfit(prices, 0) == 0)
    check(bestProfit(intArrayOf(), 3) == 0)
    check(bestProfit(intArrayOf(7), 1) == 0)
    check(bestProfit(intArrayOf(1, 2, 3, 4, 5), 2) == 4)
    check(bestProfit(zigzag, 1) == 5)
    check(bestProfit(zigzag, 2) == 8)
    check(bestProfit(zigzag, 3) == 9)
    check(bestProfit(zigzag, 10) == 9)
    check(bestProfit(intArrayOf(1, 3, 2, 8), 1) == 7)
    println("179 ok")
}
