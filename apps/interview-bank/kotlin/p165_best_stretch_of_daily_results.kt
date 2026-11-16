// 165 · Best Stretch of Daily Results
package p165

fun bestStretch(results: IntArray): Int {
    var endingHere = 0 // best total of a stretch that ends on day r; 0 before day one
    var best = results[0]
    for (r in results) {
        endingHere = maxOf(r, endingHere + r)
        best = maxOf(best, endingHere)
    }
    return best
}

fun main() {
    check(bestStretch(intArrayOf(-2, 4, -1, 3, -7, 2)) == 6)
    check(bestStretch(intArrayOf(-8, -3, -5)) == -3)
    check(bestStretch(intArrayOf(5)) == 5)
    check(bestStretch(intArrayOf(1, 2, 3)) == 6)
    check(bestStretch(intArrayOf(-1, 0, -2)) == 0)
    check(bestStretch(intArrayOf(2, -1, 2, -9, 4)) == 4)
    check(bestStretch(intArrayOf(3, -2, 5, -1)) == 6)
    println("165 ok")
}
