// 159 · Phone Keypad Words
package p159

private val KEYS = listOf("", "", "abc", "def", "ghi", "jkl", "mno", "pqrs", "tuv", "wxyz")

fun keypadWords(digits: String): List<String> {
    if (digits.isEmpty()) return emptyList()
    val result = mutableListOf<String>()
    val path = StringBuilder()
    fun walk(k: Int) {
        if (k == digits.length) {
            result.add(path.toString())
            return
        }
        for (letter in KEYS[digits[k] - '0']) {
            path.append(letter)
            walk(k + 1)
            path.setLength(path.length - 1)
        }
    }
    walk(0)
    return result
}

fun main() {
    check(keypadWords("23") == listOf("ad", "ae", "af", "bd", "be", "bf", "cd", "ce", "cf"))
    check(keypadWords("7") == listOf("p", "q", "r", "s"))
    check(keypadWords("").isEmpty())
    check(keypadWords("9") == listOf("w", "x", "y", "z"))
    val words = keypadWords("79")
    check(words.size == 16 && words.first() == "pw" && words.last() == "sz")
    check(keypadWords("234").size == 27)
    println("159 ok")
}
