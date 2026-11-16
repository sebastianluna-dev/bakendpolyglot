// 164 · Rob Houses Without Tripping the Alarm
package p164

fun maxHaul(cash: IntArray): Int {
    var skipPrev = 0 // best total over the houses before the previous one
    var best = 0 // best total over the houses seen so far
    for (c in cash) {
        val next = maxOf(best, skipPrev + c)
        skipPrev = best
        best = next
    }
    return best
}

fun main() {
    check(maxHaul(intArrayOf(6, 1, 2, 9)) == 15)
    check(maxHaul(intArrayOf(5, 10, 4, 3, 8)) == 18)
    check(maxHaul(intArrayOf()) == 0)
    check(maxHaul(intArrayOf(7)) == 7)
    check(maxHaul(intArrayOf(2, 1, 1, 2)) == 4)
    check(maxHaul(intArrayOf(4, 1, 2, 7, 5, 3, 1)) == 14)
    check(maxHaul(intArrayOf(0, 0, 0)) == 0)
    println("164 ok")
}
