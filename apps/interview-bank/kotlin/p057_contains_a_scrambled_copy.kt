// 057 · Contains a Scrambled Copy
package p057

fun containsScrambled(pattern: String, text: String): Boolean {
    val m = pattern.length
    if (m > text.length) return false
    val need = IntArray(26)
    val have = IntArray(26)
    for (i in 0 until m) {
        need[pattern[i] - 'a']++
        have[text[i] - 'a']++
    }
    if (need.contentEquals(have)) return true
    for (i in m until text.length) {
        have[text[i] - 'a']++ // letter entering the window
        have[text[i - m] - 'a']-- // letter leaving it
        if (need.contentEquals(have)) return true
    }
    return false
}

fun main() {
    check(containsScrambled("loan", "renewalnoted"))
    check(!containsScrambled("seat", "stage"))
    check(!containsScrambled("abc", "ab"))
    check(containsScrambled("ab", "ab"))
    check(containsScrambled("aab", "baxaba"))
    check(!containsScrambled("aab", "abbb"))
    check(containsScrambled("z", "abcz"))
    println("057 ok")
}
