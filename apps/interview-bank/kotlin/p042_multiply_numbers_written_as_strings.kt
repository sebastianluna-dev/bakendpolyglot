// 042 · Multiply Numbers Written as Strings
package p042

fun multiplyStrings(a: String, b: String): String {
    val digits = IntArray(a.length + b.length)
    for (i in a.indices.reversed()) {
        for (j in b.indices.reversed()) {
            val sum = (a[i] - '0') * (b[j] - '0') + digits[i + j + 1]
            digits[i + j + 1] = sum % 10
            digits[i + j] += sum / 10 // may pass 9 for now; fixed when its own turn comes
        }
    }
    var start = 0
    while (start < digits.size - 1 && digits[start] == 0) start++
    return digits.copyOfRange(start, digits.size).joinToString("")
}

fun main() {
    check(multiplyStrings("123", "45") == "5535")
    check(multiplyStrings("0", "9081") == "0")
    check(multiplyStrings("999", "999") == "998001")
    check(multiplyStrings("7", "8") == "56")
    check(multiplyStrings("1", "1") == "1")
    check(multiplyStrings("123456789", "987654321") == "121932631112635269")
    check(
        multiplyStrings("99999999999999999999", "99999999999999999999") ==
            "9999999999999999999800000000000000000001",
    )
    println("042 ok")
}
