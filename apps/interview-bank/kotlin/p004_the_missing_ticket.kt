// 004 · The Missing Ticket
package p004

fun missingTicket(scanned: IntArray): Int {
    val n = scanned.size + 1L
    var total = n * (n + 1) / 2 // Long: n * (n + 1) overflows Int above 46,340 tickets
    for (t in scanned) total -= t
    return total.toInt()
}

fun main() {
    val many = (1..70000).filter { it != 31415 }.toIntArray()

    check(missingTicket(intArrayOf(3, 1, 4, 5)) == 2)
    check(missingTicket(intArrayOf(2)) == 1)
    check(missingTicket(intArrayOf(1)) == 2)
    check(missingTicket(intArrayOf()) == 1)
    check(missingTicket(many) == 31415)
    println("004 ok")
}
