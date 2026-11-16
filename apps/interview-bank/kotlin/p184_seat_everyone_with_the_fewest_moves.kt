// 184 · Seat Everyone With the Fewest Moves
package p184

import kotlin.math.abs

fun fewestMoves(seats: IntArray, guests: IntArray): Int {
    val s = seats.sortedArray()
    val g = guests.sortedArray()
    var moves = 0
    for (i in s.indices) moves += abs(s[i] - g[i])
    return moves
}

fun main() {
    check(fewestMoves(intArrayOf(4, 1, 7), intArrayOf(2, 8, 3)) == 3)
    check(fewestMoves(intArrayOf(2, 6, 9, 10), intArrayOf(5, 5, 1, 12)) == 8)
    check(fewestMoves(intArrayOf(), intArrayOf()) == 0)
    check(fewestMoves(intArrayOf(3, 5), intArrayOf(5, 3)) == 0)
    check(fewestMoves(intArrayOf(10), intArrayOf(1)) == 9)
    println("184 ok")
}
