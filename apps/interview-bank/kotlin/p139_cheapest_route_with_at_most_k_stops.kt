// 139 · Cheapest Route With at Most k Stops
package p139

fun cheapestRoute(n: Int, legs: Array<IntArray>, source: Int, target: Int, k: Int): Int {
    var cost = IntArray(n) { Int.MAX_VALUE }
    cost[source] = 0
    repeat(k + 1) {
        val next = cost.copyOf() // relax from last round's costs only, so each round adds at most one leg
        for ((from, to, price) in legs) {
            if (cost[from] != Int.MAX_VALUE && cost[from] + price < next[to]) next[to] = cost[from] + price
        }
        cost = next
    }
    return if (cost[target] == Int.MAX_VALUE) -1 else cost[target]
}

fun main() {
    val legs = arrayOf(
        intArrayOf(0, 1, 100), intArrayOf(1, 2, 100), intArrayOf(2, 3, 100), intArrayOf(0, 2, 500), intArrayOf(1, 3, 600),
    )
    val triangle = arrayOf(intArrayOf(0, 1, 1), intArrayOf(1, 2, 1), intArrayOf(0, 2, 5))
    check(cheapestRoute(4, legs, 0, 3, 1) == 600)
    check(cheapestRoute(4, legs, 0, 3, 2) == 300)
    check(cheapestRoute(4, legs, 0, 3, 0) == -1)
    check(cheapestRoute(4, legs, 0, 3, 10) == 300)
    check(cheapestRoute(3, triangle, 0, 2, 0) == 5)
    check(cheapestRoute(3, triangle, 0, 2, 1) == 2)
    check(cheapestRoute(2, arrayOf(intArrayOf(0, 1, 7)), 1, 1, 0) == 0)
    check(cheapestRoute(3, arrayOf(intArrayOf(0, 1, 4)), 0, 2, 5) == -1)
    check(cheapestRoute(2, arrayOf(intArrayOf(1, 0, 3)), 0, 1, 3) == -1)
    println("139 ok")
}
