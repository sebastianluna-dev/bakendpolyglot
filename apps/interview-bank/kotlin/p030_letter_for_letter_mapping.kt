// 030 · Letter-for-Letter Mapping
package p030

fun canMapLetters(source: String, target: String): Boolean {
    if (source.length != target.length) return false
    val forward = HashMap<Char, Char>()
    val backward = HashMap<Char, Char>() // stops two letters mapping to the same one
    for (i in source.indices) {
        val a = source[i]
        val b = target[i]
        if (forward.getOrDefault(a, b) != b || backward.getOrDefault(b, a) != a) return false
        forward[a] = b
        backward[b] = a
    }
    return true
}

fun main() {
    check(canMapLetters("deed", "noon"))
    check(!canMapLetters("bell", "cool"))
    check(canMapLetters("", ""))
    check(!canMapLetters("a", "bc"))
    check(canMapLetters("abc", "bca"))
    check(!canMapLetters("ab", "aa"))
    check(!canMapLetters("aa", "ab"))
    check(!canMapLetters("tab", "tat"))
    println("030 ok")
}
