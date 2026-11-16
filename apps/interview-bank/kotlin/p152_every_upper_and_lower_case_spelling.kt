// 152 · Every Upper- and Lower-Case Spelling
package p152

fun allSpellings(code: String): List<String> {
    val chars = code.toCharArray()
    val out = mutableListOf<String>()
    fun spell(i: Int) {
        if (i == chars.size) {
            out.add(String(chars))
            return
        }
        val c = code[i]
        if (!c.isLetter()) { // a digit: one choice
            spell(i + 1)
            return
        }
        chars[i] = c.uppercaseChar() // upper case first keeps the output sorted
        spell(i + 1)
        chars[i] = c.lowercaseChar()
        spell(i + 1)
    }
    spell(0)
    return out
}

fun main() {
    check(allSpellings("b7x") == listOf("B7X", "B7x", "b7X", "b7x"))
    check(allSpellings("42") == listOf("42"))
    check(allSpellings("Q") == listOf("Q", "q"))
    check(allSpellings("aB1") == listOf("AB1", "Ab1", "aB1", "ab1"))
    check(allSpellings("0z0") == listOf("0Z0", "0z0"))
    check(allSpellings("abcdefghij").size == 1024)
    println("152 ok")
}
