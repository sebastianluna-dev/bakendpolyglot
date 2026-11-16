// 002 · Two Prices for a Budget
package p002

fun twoPrices(prices: IntArray, budget: Int): IntArray {
    val firstIndex = HashMap<Int, Int>() // price -> earliest index
    for ((j, price) in prices.withIndex()) {
        val i = firstIndex[budget - price]
        if (i != null) return intArrayOf(i, j)
        firstIndex.putIfAbsent(price, j)
    }
    return intArrayOf()
}

fun main() {
    check(twoPrices(intArrayOf(12, 7, 3, 9, 5), 12).contentEquals(intArrayOf(2, 3)))
    check(twoPrices(intArrayOf(4, 4, 1), 8).contentEquals(intArrayOf(0, 1)))
    check(twoPrices(intArrayOf(1, 2), 10).isEmpty())
    println("002 ok")
}
