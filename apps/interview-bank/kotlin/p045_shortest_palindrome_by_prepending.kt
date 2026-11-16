// 045 · Shortest Palindrome by Prepending
package p045

fun shortestPalindrome(s: String): String {
    val reversed = s.reversed()
    val t = "$s#$reversed" // the '#' stops a match from running past the end of s
    val fail = IntArray(t.length)
    var k = 0
    for (i in 1 until t.length) {
        while (k > 0 && t[i] != t[k]) k = fail[k - 1]
        if (t[i] == t[k]) k++
        fail[i] = k
    }
    val keep = fail[t.length - 1] // length of the longest palindromic prefix of s
    return reversed.substring(0, s.length - keep) + s
}

fun main() {
    check(shortestPalindrome("abacd") == "dcabacd")
    check(shortestPalindrome("stats") == "stats")
    check(shortestPalindrome("") == "")
    check(shortestPalindrome("a") == "a")
    check(shortestPalindrome("ab") == "bab")
    check(shortestPalindrome("aabba") == "abbaabba")
    check(shortestPalindrome("aaa") == "aaa")
    println("045 ok")
}
