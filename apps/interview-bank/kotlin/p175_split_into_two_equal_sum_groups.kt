// 175 · Split Into Two Equal-Sum Groups
package p175

fun canSplitEvenly(weights: IntArray): Boolean {
    val total = weights.sum()
    if (total % 2 != 0) return false
    val half = total / 2
    val reachable = BooleanArray(half + 1) // reachable[s]: some parcels seen so far weigh s
    reachable[0] = true
    for (w in weights) {
        for (s in half downTo w) { // downwards, so this parcel is counted at most once
            if (reachable[s - w]) reachable[s] = true
        }
    }
    return reachable[half]
}

fun main() {
    check(canSplitEvenly(intArrayOf(3, 1, 4, 2, 2)))
    check(!canSplitEvenly(intArrayOf(2, 3, 7)))
    check(!canSplitEvenly(intArrayOf(1)))
    check(canSplitEvenly(intArrayOf(5, 5)))
    check(!canSplitEvenly(intArrayOf(1, 5)))
    check(!canSplitEvenly(intArrayOf(1, 2, 4)))
    check(canSplitEvenly(intArrayOf(1, 2, 3, 4, 5, 6, 7)))
    check(!canSplitEvenly(intArrayOf(6, 6, 6, 6, 6, 4)))
    check(canSplitEvenly(IntArray(200) { 100 }))
    println("175 ok")
}
