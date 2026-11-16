// 076 · Colliding Parcels on a Conveyor
package p076

fun survivingParcels(parcels: IntArray): IntArray {
    val track = ArrayDeque<Int>()
    for (p in parcels) {
        var alive = true
        while (alive && p < 0 && track.isNotEmpty() && track.last() > 0) {
            val top = track.last()
            when {
                top < -p -> track.removeLast()
                top == -p -> {
                    track.removeLast()
                    alive = false
                }
                else -> alive = false
            }
        }
        if (alive) track.addLast(p)
    }
    return track.toIntArray()
}

fun main() {
    check(survivingParcels(intArrayOf(4, 7, -5)).contentEquals(intArrayOf(4, 7)))
    check(survivingParcels(intArrayOf(3, 6, -6, -8, 2)).contentEquals(intArrayOf(-8, 2)))
    check(survivingParcels(intArrayOf()).isEmpty())
    check(survivingParcels(intArrayOf(-3, 4)).contentEquals(intArrayOf(-3, 4)))
    check(survivingParcels(intArrayOf(5, -5)).isEmpty())
    check(survivingParcels(intArrayOf(1, -2, -2, -2)).contentEquals(intArrayOf(-2, -2, -2)))
    check(survivingParcels(intArrayOf(10, 2, -5)).contentEquals(intArrayOf(10)))
    println("076 ok")
}
