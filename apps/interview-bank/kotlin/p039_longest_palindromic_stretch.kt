// 039 · Longest Palindromic Stretch
package p039

fun longestPalindrome(s: String): String {
    var bestStart = 0
    var bestLength = 0
    for (i in s.indices) {
        for (length in intArrayOf(expand(s, i, i), expand(s, i, i + 1))) {
            if (length > bestLength) { // strictly longer, so the first stretch wins a tie
                bestLength = length
                bestStart = i - (length - 1) / 2
            }
        }
    }
    return s.substring(bestStart, bestStart + bestLength)
}

private fun expand(s: String, lo: Int, hi: Int): Int {
    var l = lo
    var h = hi
    while (l >= 0 && h < s.length && s[l] == s[h]) {
        l--
        h++
    }
    return h - l - 1
}

fun main() {
    check(longestPalindrome("xabbaracecarz") == "racecar")
    check(longestPalindrome("abcde") == "a")
    check(longestPalindrome("xyzzyabba") == "yzzy")
    check(longestPalindrome("banana") == "anana")
    check(longestPalindrome("abacab") == "bacab")
    check(longestPalindrome("aaaa") == "aaaa")
    check(longestPalindrome("a") == "a")
    check(longestPalindrome("") == "")
    println("039 ok")
}
