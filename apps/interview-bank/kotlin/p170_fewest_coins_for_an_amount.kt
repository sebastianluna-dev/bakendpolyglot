// 170 · Fewest Coins for an Amount
package p170

fun fewestCoins(coins: IntArray, amount: Int): Int {
    val unreachable = amount + 1 // more coins than any real answer needs
    val fewest = IntArray(amount + 1) { unreachable }
    fewest[0] = 0
    for (a in 1..amount) {
        for (coin in coins) {
            if (coin <= a) fewest[a] = minOf(fewest[a], fewest[a - coin] + 1)
        }
    }
    return if (fewest[amount] == unreachable) -1 else fewest[amount]
}

fun main() {
    check(fewestCoins(intArrayOf(1, 4, 6), 8) == 2)
    check(fewestCoins(intArrayOf(5, 10), 3) == -1)
    check(fewestCoins(intArrayOf(7), 0) == 0)
    check(fewestCoins(intArrayOf(2), 3) == -1)
    check(fewestCoins(intArrayOf(3, 7), 20) == 4)
    check(fewestCoins(intArrayOf(2, 5, 10, 25), 99) == 7)
    check(fewestCoins(intArrayOf(4, 7), 5000) == 716)
    println("170 ok")
}
