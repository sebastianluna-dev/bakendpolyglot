// 037 · Numbers to Roman Numerals
package p037

private val VALUES = intArrayOf(1000, 900, 500, 400, 100, 90, 50, 40, 10, 9, 5, 4, 1)
private val SYMBOLS = arrayOf("M", "CM", "D", "CD", "C", "XC", "L", "XL", "X", "IX", "V", "IV", "I")

fun toRoman(n: Int): String = buildString {
    var rest = n
    for (i in VALUES.indices) {
        while (rest >= VALUES[i]) {
            append(SYMBOLS[i])
            rest -= VALUES[i]
        }
    }
}

fun main() {
    check(toRoman(2026) == "MMXXVI")
    check(toRoman(1994) == "MCMXCIV")
    check(toRoman(1) == "I")
    check(toRoman(4) == "IV")
    check(toRoman(49) == "XLIX")
    check(toRoman(58) == "LVIII")
    check(toRoman(444) == "CDXLIV")
    check(toRoman(3999) == "MMMCMXCIX")
    println("037 ok")
}
