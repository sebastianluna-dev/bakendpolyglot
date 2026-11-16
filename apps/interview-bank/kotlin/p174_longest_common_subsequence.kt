// 174 · Longest Common Subsequence
package p174

fun longestCommonSubsequence(a: String, b: String): Int {
    val m = b.length
    // cur[j]: answer for the first i letters of a and the first j of b; prev holds row i - 1
    var prev = IntArray(m + 1)
    for (i in 1..a.length) {
        val cur = IntArray(m + 1)
        for (j in 1..m) {
            cur[j] = if (a[i - 1] == b[j - 1]) prev[j - 1] + 1 else maxOf(prev[j], cur[j - 1])
        }
        prev = cur
    }
    return prev[m]
}

fun main() {
    check(longestCommonSubsequence("parcel", "place") == 4)
    check(longestCommonSubsequence("seat", "lock") == 0)
    check(longestCommonSubsequence("", "abc") == 0)
    check(longestCommonSubsequence("abc", "") == 0)
    check(longestCommonSubsequence("member", "member") == 6)
    check(longestCommonSubsequence("aaaa", "aa") == 2)
    check(longestCommonSubsequence("delivery", "relived") == 5)
    println("174 ok")
}
