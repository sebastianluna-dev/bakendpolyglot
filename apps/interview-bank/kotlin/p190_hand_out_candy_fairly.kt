// 190 · Hand Out Candy Fairly
package p190

fun minCandy(scores: IntArray): Int {
    val n = scores.size
    val candy = IntArray(n) { 1 }
    for (i in 1 until n) {
        if (scores[i] > scores[i - 1]) candy[i] = candy[i - 1] + 1
    }
    for (i in n - 2 downTo 0) {
        // max keeps what the first pass gave for the left neighbour
        if (scores[i] > scores[i + 1]) candy[i] = maxOf(candy[i], candy[i + 1] + 1)
    }
    return candy.sum()
}

fun main() {
    check(minCandy(intArrayOf(1, 3, 2, 2, 1)) == 7)
    check(minCandy(intArrayOf(5, 4, 3, 4)) == 8)
    check(minCandy(intArrayOf()) == 0)
    check(minCandy(intArrayOf(7)) == 1)
    check(minCandy(intArrayOf(2, 2, 2)) == 3)
    check(minCandy(intArrayOf(1, 2, 3, 4)) == 10)
    check(minCandy(intArrayOf(1, 2, 3, 4, 1)) == 11)
    check(minCandy(intArrayOf(4, 3, 2, 1)) == 10)
    println("190 ok")
}
