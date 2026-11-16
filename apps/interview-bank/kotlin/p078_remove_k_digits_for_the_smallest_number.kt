// 078 · Remove k Digits for the Smallest Number
package p078

fun removeKDigits(num: String, k: Int): String {
    var left = k
    val kept = StringBuilder() // used as a stack of digits
    for (d in num) {
        while (left > 0 && kept.isNotEmpty() && kept.last() > d) {
            kept.setLength(kept.length - 1)
            left--
        }
        kept.append(d)
    }
    kept.setLength(kept.length - left) // digits never fell, so drop the rest from the end
    var start = 0
    while (start < kept.length - 1 && kept[start] == '0') start++
    return if (kept.isEmpty()) "0" else kept.substring(start)
}

fun main() {
    check(removeKDigits("4172", 2) == "12")
    check(removeKDigits("30200", 1) == "200")
    check(removeKDigits("9", 1) == "0")
    check(removeKDigits("12345", 2) == "123")
    check(removeKDigits("10", 1) == "0")
    check(removeKDigits("1432219", 0) == "1432219")
    check(removeKDigits("112", 1) == "11")
    check(removeKDigits("100200", 1) == "200")
    println("078 ok")
}
