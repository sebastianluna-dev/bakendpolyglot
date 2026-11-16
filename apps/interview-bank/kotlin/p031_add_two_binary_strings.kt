// 031 · Add Two Binary Strings
package p031

fun addBinary(a: String, b: String): String {
    val digits = StringBuilder()
    var i = a.length - 1
    var j = b.length - 1
    var carry = 0
    while (i >= 0 || j >= 0 || carry > 0) {
        var total = carry
        if (i >= 0) total += a[i--] - '0'
        if (j >= 0) total += b[j--] - '0'
        digits.append(total % 2)
        carry = total / 2
    }
    return digits.reverse().toString()
}

fun main() {
    check(addBinary("1011", "111") == "10010")
    check(addBinary("0", "0") == "0")
    check(addBinary("1", "1") == "10")
    check(addBinary("1111", "1") == "10000")
    check(addBinary("100", "110010") == "110110")
    check(addBinary("1".repeat(70), "1") == "1" + "0".repeat(70))
    println("031 ok")
}
