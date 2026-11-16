// 006 · The Unpaired Seat
package p006

fun unpairedSeat(log: IntArray): Int {
    var result = 0
    for (seat in log) result = result xor seat // pairs cancel: x xor x is 0
    return result
}

fun main() {
    check(unpairedSeat(intArrayOf(14, 3, 14, 8, 3)) == 8)
    check(unpairedSeat(intArrayOf(21)) == 21)
    check(unpairedSeat(intArrayOf(4, 4, 9)) == 9)
    check(unpairedSeat(intArrayOf(7, 2, 2)) == 7)
    check(unpairedSeat(intArrayOf(1000000000, 5, 5)) == 1000000000)
    println("006 ok")
}
