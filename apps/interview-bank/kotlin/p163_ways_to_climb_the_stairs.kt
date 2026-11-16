// 163 · Ways to Climb the Stairs
package p163

fun waysToClimb(n: Int): Long {
    var prev = 1L // ways to reach step i - 2
    var cur = 1L // ways to reach step i - 1
    for (i in 2..n) {
        val next = prev + cur
        prev = cur
        cur = next
    }
    return cur
}

fun main() {
    check(waysToClimb(4) == 5L)
    check(waysToClimb(1) == 1L)
    check(waysToClimb(0) == 1L)
    check(waysToClimb(2) == 2L)
    check(waysToClimb(10) == 89L)
    check(waysToClimb(46) == 2971215073L)
    check(waysToClimb(70) == 308061521170129L)
    println("163 ok")
}
