// 173 · Ways to Decode a Digit String
package p173

fun countDecodings(digits: String): Int {
    var twoBack = 0 // decodings of the first i - 2 digits
    var oneBack = 1 // decodings of the first i - 1 digits; the empty prefix has one
    for (i in 1..digits.length) {
        var ways = if (digits[i - 1] != '0') oneBack else 0
        if (i >= 2) {
            val two = digits.substring(i - 2, i).toInt()
            if (two in 10..26) ways += twoBack // at least 10: "06" is not a letter
        }
        twoBack = oneBack
        oneBack = ways
    }
    return oneBack
}

fun main() {
    check(countDecodings("2612") == 4)
    check(countDecodings("301") == 0)
    check(countDecodings("1201") == 1)
    check(countDecodings("106") == 1)
    check(countDecodings("0") == 0)
    check(countDecodings("100") == 0)
    check(countDecodings("27") == 1)
    check(countDecodings("11106") == 2)
    check(countDecodings("1".repeat(45)) == 1836311903)
    println("173 ok")
}
