// 102 · Split Parcels Across k Trucks With the Lightest Peak
package p102

fun lightestPeak(weights: IntArray, k: Int): Int {
    var lo = weights.max()
    var hi = weights.sum()
    while (lo < hi) {
        val mid = lo + (hi - lo) / 2
        if (trucksNeeded(weights, mid) <= k) hi = mid else lo = mid + 1
    }
    return lo
}

fun trucksNeeded(weights: IntArray, limit: Int): Int {
    var trucks = 1
    var load = 0
    for (w in weights) {
        if (load + w > limit) {
            trucks++
            load = 0
        }
        load += w
    }
    return trucks
}

fun main() {
    check(lightestPeak(intArrayOf(4, 9, 3, 6, 8, 2), 3) == 13)
    check(lightestPeak(intArrayOf(3, 1, 12, 2), 3) == 12)
    check(lightestPeak(intArrayOf(4, 9, 3), 1) == 16)
    check(lightestPeak(intArrayOf(2, 8, 5), 3) == 8)
    check(lightestPeak(intArrayOf(7), 1) == 7)
    check(lightestPeak(intArrayOf(1, 1, 1, 1, 1, 1), 4) == 2)
    println("102 ok")
}
