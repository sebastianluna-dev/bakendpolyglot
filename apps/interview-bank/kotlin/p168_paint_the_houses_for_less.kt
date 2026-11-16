// 168 · Paint the Houses for Less
package p168

fun minPaintCost(costs: Array<IntArray>): Int {
    var red = 0 // cheapest total so far if the last house painted is red
    var green = 0
    var blue = 0
    for ((r, g, b) in costs) {
        val nextRed = r + minOf(green, blue)
        val nextGreen = g + minOf(red, blue)
        val nextBlue = b + minOf(red, green)
        red = nextRed
        green = nextGreen
        blue = nextBlue
    }
    return minOf(red, green, blue)
}

fun main() {
    check(minPaintCost(arrayOf(intArrayOf(8, 3, 6), intArrayOf(4, 9, 2), intArrayOf(7, 5, 1))) == 8)
    check(minPaintCost(arrayOf(intArrayOf(1, 2, 9), intArrayOf(1, 9, 9))) == 3)
    check(minPaintCost(arrayOf()) == 0)
    check(minPaintCost(arrayOf(intArrayOf(5, 1, 9))) == 1)
    check(minPaintCost(arrayOf(intArrayOf(7, 6, 2), intArrayOf(3, 8, 9), intArrayOf(4, 1, 6), intArrayOf(9, 2, 3))) == 9)
    check(minPaintCost(arrayOf(intArrayOf(4, 4, 4), intArrayOf(4, 4, 4))) == 8)
    println("168 ok")
}
