// 136 · Exchange Rates From Known Pairs
package p136

import kotlin.math.abs

fun exchangeRates(pairs: List<List<String>>, rates: DoubleArray, queries: List<List<String>>): DoubleArray {
    val graph = HashMap<String, MutableList<Pair<String, Double>>>()
    for ((i, pair) in pairs.withIndex()) {
        val (from, to) = pair
        graph.getOrPut(from) { mutableListOf() }.add(to to rates[i])
        graph.getOrPut(to) { mutableListOf() }.add(from to 1 / rates[i])
    }
    val group = HashMap<String, Int>()
    val value = HashMap<String, Double>() // units of this currency worth one unit of its group's first
    for (start in graph.keys) {
        if (start in group) continue
        val id = group.size
        group[start] = id
        value[start] = 1.0
        val queue = ArrayDeque(listOf(start))
        while (queue.isNotEmpty()) {
            val cur = queue.removeFirst()
            for ((next, rate) in graph.getValue(cur)) {
                if (next in group) continue
                group[next] = id
                value[next] = value.getValue(cur) * rate
                queue.addLast(next)
            }
        }
    }
    return DoubleArray(queries.size) { i ->
        val (x, y) = queries[i]
        if (x in group && group[x] == group[y]) value.getValue(y) / value.getValue(x) else -1.0
    }
}

fun near(got: DoubleArray, want: DoubleArray): Boolean =
    got.size == want.size && got.indices.all { abs(got[it] - want[it]) < 1e-9 }

fun main() {
    check(
        near(
            exchangeRates(
                listOf(listOf("EUR", "USD"), listOf("USD", "MXN")),
                doubleArrayOf(1.25, 20.0),
                listOf(listOf("EUR", "MXN"), listOf("MXN", "EUR"), listOf("USD", "USD"), listOf("EUR", "JPY"), listOf("JPY", "JPY")),
            ),
            doubleArrayOf(25.0, 0.04, 1.0, -1.0, -1.0),
        ),
    )
    check(
        near(
            exchangeRates(
                listOf(listOf("GBP", "EUR"), listOf("CHF", "SEK"), listOf("SEK", "NOK")),
                doubleArrayOf(1.2, 12.0, 0.5),
                listOf(listOf("EUR", "GBP"), listOf("CHF", "NOK"), listOf("NOK", "CHF"), listOf("GBP", "NOK")),
            ),
            doubleArrayOf(0.8333333333, 6.0, 0.1666666667, -1.0),
        ),
    )
    check(near(exchangeRates(listOf(listOf("EUR", "USD")), doubleArrayOf(1.25), listOf()), doubleArrayOf()))
    check(
        near(
            exchangeRates(
                listOf(listOf("USD", "CAD"), listOf("CAD", "AUD"), listOf("USD", "AUD")),
                doubleArrayOf(1.5, 1.2, 1.8),
                listOf(listOf("AUD", "USD"), listOf("CAD", "CAD")),
            ),
            doubleArrayOf(0.5555555556, 1.0),
        ),
    )
    println("136 ok")
}
