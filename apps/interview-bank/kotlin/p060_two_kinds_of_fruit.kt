// 060 · Two Kinds of Fruit
package p060

fun longestTwoKinds(crates: IntArray): Int {
    val inWindow = HashMap<Int, Int>() // kind -> crates of it in the window
    var left = 0
    var best = 0
    for (right in crates.indices) {
        inWindow[crates[right]] = inWindow.getOrDefault(crates[right], 0) + 1
        while (inWindow.size > 2) {
            val kind = crates[left]
            val count = inWindow.getValue(kind) - 1
            if (count == 0) {
                inWindow.remove(kind) // a kind with no crates left must not count
            } else {
                inWindow[kind] = count
            }
            left++
        }
        best = maxOf(best, right - left + 1)
    }
    return best
}

fun main() {
    check(longestTwoKinds(intArrayOf(3, 3, 7, 3, 9, 9, 7)) == 4)
    check(longestTwoKinds(intArrayOf(5, 5, 5)) == 3)
    check(longestTwoKinds(intArrayOf()) == 0)
    check(longestTwoKinds(intArrayOf(1, 2, 3, 4)) == 2)
    check(longestTwoKinds(intArrayOf(1, 2, 1, 3, 3, 3, 1)) == 5)
    check(longestTwoKinds(intArrayOf(4)) == 1)
    println("060 ok")
}
