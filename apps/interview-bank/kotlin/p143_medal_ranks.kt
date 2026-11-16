// 143 · Medal Ranks
package p143

import java.util.PriorityQueue

fun medalRanks(scores: IntArray): Array<String> {
    val heap = PriorityQueue<Int>(compareByDescending { scores[it] }) // indices, highest score on top
    for (i in scores.indices) heap.add(i)
    val medals = listOf("Gold Medal", "Silver Medal", "Bronze Medal")
    val ranks = Array(scores.size) { "" }
    var place = 1
    while (heap.isNotEmpty()) {
        val i = heap.poll()
        ranks[i] = if (place <= 3) medals[place - 1] else place.toString()
        place++
    }
    return ranks
}

fun main() {
    check(medalRanks(intArrayOf(88, 95, 70, 91)).contentEquals(arrayOf("Bronze Medal", "Gold Medal", "4", "Silver Medal")))
    check(
        medalRanks(intArrayOf(3, 9, 1, 12, 5, 7))
            .contentEquals(arrayOf("5", "Silver Medal", "6", "Gold Medal", "4", "Bronze Medal")),
    )
    check(medalRanks(intArrayOf(42)).contentEquals(arrayOf("Gold Medal")))
    check(medalRanks(intArrayOf(0, 5)).contentEquals(arrayOf("Silver Medal", "Gold Medal")))
    check(medalRanks(intArrayOf()).isEmpty())
    check(
        medalRanks(intArrayOf(10, 20, 30, 40, 50, 60, 70, 80, 90, 100, 110))
            .contentEquals(arrayOf("11", "10", "9", "8", "7", "6", "5", "4", "Bronze Medal", "Silver Medal", "Gold Medal")),
    )
    println("143 ok")
}
