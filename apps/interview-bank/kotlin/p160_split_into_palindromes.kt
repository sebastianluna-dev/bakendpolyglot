// 160 · Split Into Palindromes
package p160

fun palindromeSplits(s: String): List<List<String>> {
    val result = mutableListOf<List<String>>()
    val path = mutableListOf<String>()
    fun isPalindrome(from: Int, to: Int): Boolean {
        var lo = from
        var hi = to
        while (lo < hi) {
            if (s[lo] != s[hi]) return false
            lo++
            hi--
        }
        return true
    }
    fun walk(start: Int) {
        if (start == s.length) {
            result.add(path.toList())
            return
        }
        for (end in start until s.length) {
            if (!isPalindrome(start, end)) continue
            path.add(s.substring(start, end + 1))
            walk(end + 1)
            path.removeAt(path.lastIndex)
        }
    }
    walk(0)
    return result
}

fun main() {
    check(palindromeSplits("aab") == listOf(listOf("a", "a", "b"), listOf("aa", "b")))
    check(palindromeSplits("noon") == listOf(listOf("n", "o", "o", "n"), listOf("n", "oo", "n"), listOf("noon")))
    check(palindromeSplits("a") == listOf(listOf("a")))
    check(palindromeSplits("ab") == listOf(listOf("a", "b")))
    check(palindromeSplits("aaa") == listOf(listOf("a", "a", "a"), listOf("a", "aa"), listOf("aa", "a"), listOf("aaa")))
    check(palindromeSplits("racecar").last() == listOf("racecar"))
    println("160 ok")
}
