// 125 · Find the Trusted Judge
package p125

fun findJudge(n: Int, trust: Array<IntArray>): Int {
    val score = IntArray(n + 1) // trusted by, minus trusts
    for ((a, b) in trust) {
        score[a]--
        score[b]++
    }
    for (person in 1..n) {
        if (score[person] == n - 1) return person
    }
    return -1
}

fun main() {
    check(findJudge(4, arrayOf(intArrayOf(1, 3), intArrayOf(2, 3), intArrayOf(4, 3), intArrayOf(4, 1))) == 3)
    check(findJudge(3, arrayOf(intArrayOf(1, 3), intArrayOf(2, 3), intArrayOf(3, 1))) == -1)
    check(findJudge(1, arrayOf()) == 1)
    check(findJudge(2, arrayOf()) == -1)
    check(findJudge(2, arrayOf(intArrayOf(1, 2), intArrayOf(2, 1))) == -1)
    check(findJudge(3, arrayOf(intArrayOf(1, 2), intArrayOf(2, 3))) == -1)
    println("125 ok")
}
