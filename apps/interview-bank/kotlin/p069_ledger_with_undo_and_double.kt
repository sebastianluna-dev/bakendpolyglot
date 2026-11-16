// 069 · Ledger With Undo and Double
package p069

fun ledgerTotal(commands: Array<String>): Int {
    val amounts = ArrayDeque<Int>()
    for (cmd in commands) {
        when (cmd) {
            "U" -> amounts.removeLast()
            "D" -> amounts.addLast(2 * amounts.last())
            "+" -> amounts.addLast(amounts[amounts.size - 1] + amounts[amounts.size - 2])
            else -> amounts.addLast(cmd.toInt())
        }
    }
    return amounts.sum()
}

fun main() {
    check(ledgerTotal(arrayOf("5", "-2", "D", "+", "U", "10", "+")) == 15)
    check(ledgerTotal(arrayOf("7", "U")) == 0)
    check(ledgerTotal(arrayOf<String>()) == 0)
    check(ledgerTotal(arrayOf("100")) == 100)
    check(ledgerTotal(arrayOf("2", "3", "+")) == 10)
    check(ledgerTotal(arrayOf("3", "D", "D", "U", "+")) == 18)
    check(ledgerTotal(arrayOf("-4", "D", "U", "U")) == 0)
    println("069 ok")
}
