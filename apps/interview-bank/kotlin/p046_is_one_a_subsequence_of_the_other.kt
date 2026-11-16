// 046 · Is One a Subsequence of the Other?
package p046

fun isSubsequence(plan: String, scans: String): Boolean {
    var i = 0
    var j = 0
    while (i < plan.length && j < scans.length) {
        if (plan[i] == scans[j]) i++
        j++
    }
    return i == plan.length
}

fun main() {
    check(isSubsequence("ACE", "ABCDE"))
    check(!isSubsequence("AEC", "ABCDE"))
    check(isSubsequence("", "ABC"))
    check(!isSubsequence("A", ""))
    check(isSubsequence("AA", "ABA"))
    check(!isSubsequence("AAB", "ABAC"))
    println("046 ok")
}
