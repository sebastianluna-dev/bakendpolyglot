// 074 · Expand Repeated Patterns
package p074

fun expandPatterns(s: String): String {
    val counts = ArrayDeque<Int>()
    val outer = ArrayDeque<StringBuilder>() // the text built before each open bracket
    var current = StringBuilder()
    var count = 0
    for (ch in s) {
        when {
            ch in '0'..'9' -> count = count * 10 + (ch - '0')
            ch == '[' -> {
                counts.addLast(count)
                outer.addLast(current)
                current = StringBuilder()
                count = 0
            }
            ch == ']' -> {
                val inner = current.toString()
                current = outer.removeLast().append(inner.repeat(counts.removeLast()))
            }
            else -> current.append(ch)
        }
    }
    return current.toString()
}

fun main() {
    check(expandPatterns("3[ab]2[c]") == "abababcc")
    check(expandPatterns("x2[y3[z]]") == "xyzzzyzzz")
    check(expandPatterns("") == "")
    check(expandPatterns("plain") == "plain")
    check(expandPatterns("12[q]") == "qqqqqqqqqqqq")
    check(expandPatterns("1[a1[b1[c]]]d") == "abcd")
    check(expandPatterns("2[a2[b]c]") == "abbcabbc")
    println("074 ok")
}
