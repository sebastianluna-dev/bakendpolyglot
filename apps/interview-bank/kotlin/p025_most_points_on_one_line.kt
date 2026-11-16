// 025 · Most Points on One Line
package p025

import kotlin.math.abs

fun maxPointsOnLine(points: Array<IntArray>): Int {
    var best = 0
    for (i in points.indices) {
        val directions = HashMap<Pair<Int, Int>, Int>() // reduced direction -> points after i on it
        var same = 1 // point i and its copies
        var most = 0
        for (j in i + 1 until points.size) {
            var dx = points[j][0] - points[i][0]
            var dy = points[j][1] - points[i][1]
            if (dx == 0 && dy == 0) {
                same++
                continue
            }
            val g = gcd(abs(dx), abs(dy))
            dx /= g
            dy /= g
            // (1, -1) and (-1, 1) are the same line: make dx positive, or dy if dx is 0
            if (dx < 0 || (dx == 0 && dy < 0)) {
                dx = -dx
                dy = -dy
            }
            val count = directions.getOrDefault(dx to dy, 0) + 1
            directions[dx to dy] = count
            most = maxOf(most, count)
        }
        best = maxOf(best, same + most)
    }
    return best
}

fun gcd(a: Int, b: Int): Int = if (b == 0) a else gcd(b, a % b)

fun points(vararg p: IntArray): Array<IntArray> = arrayOf(*p)

fun main() {
    check(maxPointsOnLine(points(intArrayOf(1, 1), intArrayOf(2, 3), intArrayOf(3, 5), intArrayOf(4, 2), intArrayOf(0, -1))) == 4)
    check(maxPointsOnLine(points(intArrayOf(3, 4), intArrayOf(3, 4), intArrayOf(3, -1), intArrayOf(0, 0))) == 3)
    check(maxPointsOnLine(points()) == 0)
    check(maxPointsOnLine(points(intArrayOf(5, 5))) == 1)
    check(maxPointsOnLine(points(intArrayOf(1, 1), intArrayOf(1, 1), intArrayOf(1, 1))) == 3)
    check(maxPointsOnLine(points(intArrayOf(0, 0), intArrayOf(1, 1), intArrayOf(2, 0))) == 2)
    check(maxPointsOnLine(points(intArrayOf(-2, 7), intArrayOf(0, 7), intArrayOf(9, 7), intArrayOf(1, 1))) == 3)
    check(maxPointsOnLine(points(intArrayOf(0, 0), intArrayOf(1, -1), intArrayOf(-1, 1), intArrayOf(2, -2))) == 4)
    println("025 ok")
}
