// 056 · Longest Run After k Replacements
package p056

fun longestRunAfterReplacements(s: String, k: Int): Int {
    val counts = IntArray(26)
    var left = 0
    var best = 0
    for (right in s.indices) {
        counts[s[right] - 'A']++
        // Letters to replace = window length minus the most common letter in it.
        while (right - left + 1 - counts.max() > k) {
            counts[s[left] - 'A']--
            left++
        }
        best = maxOf(best, right - left + 1)
    }
    return best
}

fun main() {
    check(longestRunAfterReplacements("ABAACBA", 1) == 4)
    check(longestRunAfterReplacements("XYXY", 2) == 4)
    check(longestRunAfterReplacements("", 0) == 0)
    check(longestRunAfterReplacements("AAAA", 0) == 4)
    check(longestRunAfterReplacements("ABCD", 0) == 1)
    check(longestRunAfterReplacements("AB", 5) == 2)
    check(longestRunAfterReplacements("BAAABBBBA", 2) == 6)
    println("056 ok")
}
