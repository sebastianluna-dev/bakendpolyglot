// 029 · Roman Numerals to Numbers
package p029

val VALUES = mapOf('I' to 1, 'V' to 5, 'X' to 10, 'L' to 50, 'C' to 100, 'D' to 500, 'M' to 1000)

fun romanToNumber(roman: String): Int {
    var total = 0
    for (i in roman.indices) {
        val value = VALUES.getValue(roman[i])
        val next = if (i + 1 < roman.length) VALUES.getValue(roman[i + 1]) else 0
        // a smaller symbol before a larger one is subtracted (IV = 5 - 1)
        total += if (value < next) -value else value
    }
    return total
}

fun main() {
    check(romanToNumber("MMXXVI") == 2026)
    check(romanToNumber("CDXLIV") == 444)
    check(romanToNumber("I") == 1)
    check(romanToNumber("IV") == 4)
    check(romanToNumber("IX") == 9)
    check(romanToNumber("XIV") == 14)
    check(romanToNumber("CM") == 900)
    check(romanToNumber("MMMCMXCIX") == 3999)
    println("029 ok")
}
