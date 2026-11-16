// 180 · Count Distinct Subsequences
package p180

private const val MOD = 1_000_000_007

fun countSubsequences(s: String, t: String): Int {
    val m = t.length
    val ways = IntArray(m + 1) // ways[j]: ways to spell the first j letters of t so far
    ways[0] = 1
    for (c in s) {
        for (j in m downTo 1) { // downwards, so this letter of s is used at most once
            if (t[j - 1] == c) ways[j] = (ways[j] + ways[j - 1]) % MOD
        }
    }
    return ways[m]
}

fun main() {
    check(countSubsequences("banana", "ban") == 3)
    check(countSubsequences("seatseat", "sat") == 4)
    check(countSubsequences("abc", "abcd") == 0)
    check(countSubsequences("abc", "") == 1)
    check(countSubsequences("", "a") == 0)
    check(countSubsequences("", "") == 1)
    check(countSubsequences("aaaa", "aa") == 6)
    check(countSubsequences("a", "aa") == 0)
    check(countSubsequences("a".repeat(1000), "a".repeat(500)) == 159835829)
    println("180 ok")
}
