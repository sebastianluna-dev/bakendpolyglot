// 038 · Parse an Integer Strictly
package p038

fun parseInteger(s: String): Int? {
    var i = 0
    var negative = false
    if (s.isNotEmpty() && (s[0] == '+' || s[0] == '-')) {
        negative = s[0] == '-'
        i = 1
    }
    if (i == s.length) return null // empty, or a sign with no digits
    val limit = if (negative) 2147483648L else Int.MAX_VALUE.toLong()
    var value = 0L
    while (i < s.length) {
        val ch = s[i]
        if (ch < '0' || ch > '9') return null
        value = value * 10 + (ch - '0')
        if (value > limit) return null
        i++
    }
    return (if (negative) -value else value).toInt()
}

fun main() {
    check(parseInteger("42") == 42)
    check(parseInteger("-17") == -17)
    check(parseInteger("+8") == 8)
    check(parseInteger("0042") == 42)
    check(parseInteger("-0") == 0)
    check(parseInteger("2147483647") == Int.MAX_VALUE)
    check(parseInteger("-2147483648") == Int.MIN_VALUE)
    check(parseInteger("2147483648") == null)
    check(parseInteger("-2147483649") == null)
    check(parseInteger("99999999999999999999") == null)
    check(parseInteger("") == null)
    check(parseInteger("+") == null)
    check(parseInteger(" 42") == null)
    check(parseInteger("42 ") == null)
    check(parseInteger("12a") == null)
    check(parseInteger("+-3") == null)
    check(parseInteger("3.0") == null)
    println("038 ok")
}
