// 178 · Pattern Matching With . and *
package p178

fun matchesPattern(text: String, pattern: String): Boolean {
    val m = pattern.length
    // cur[j]: the first i letters of text match the first j characters of pattern; prev holds row i - 1
    var prev = BooleanArray(m + 1)
    for (i in 0..text.length) {
        val cur = BooleanArray(m + 1)
        cur[0] = i == 0
        for (j in 1..m) {
            cur[j] = if (pattern[j - 1] == '*') {
                // the starred item matches nothing, or it takes text[i - 1] and may take more
                cur[j - 2] || (i > 0 && fits(text[i - 1], pattern[j - 2]) && prev[j])
            } else {
                i > 0 && fits(text[i - 1], pattern[j - 1]) && prev[j - 1]
            }
        }
        prev = cur
    }
    return prev[m]
}

private fun fits(c: Char, p: Char): Boolean = p == '.' || p == c

fun main() {
    check(matchesPattern("seat", "se.*t"))
    check(!matchesPattern("seats", "se.*t"))
    check(matchesPattern("", "a*b*"))
    check(matchesPattern("", ""))
    check(!matchesPattern("a", ""))
    check(!matchesPattern("", "."))
    check(matchesPattern("xxy", "z*x*y"))
    check(!matchesPattern("ac", "a.c"))
    check(matchesPattern("aaa", "a*a"))
    check(matchesPattern("bbb", "bc*b*d*b"))
    check(matchesPattern("b", "a*b"))
    check(matchesPattern("ab", ".*"))
    check(!matchesPattern("abcd", "d*"))
    check(!matchesPattern("aaaaaaaaaaaaaaaaaaab", "a*a*a*a*a*a*a*a*c"))
    println("178 ok")
}
