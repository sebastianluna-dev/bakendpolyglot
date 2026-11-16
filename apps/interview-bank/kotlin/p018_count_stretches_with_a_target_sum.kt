// 018 · Count Stretches With a Target Sum
package p018

fun countStretches(changes: IntArray, target: Int): Int {
    val seen = hashMapOf(0 to 1) // prefix sum -> how many times; 0 is the empty prefix
    var sum = 0
    var count = 0
    for (change in changes) {
        sum += change
        count += seen[sum - target] ?: 0
        seen[sum] = (seen[sum] ?: 0) + 1
    }
    return count
}

fun main() {
    check(countStretches(intArrayOf(1, 2, 3, -2, 2), 3) == 4)
    check(countStretches(intArrayOf(0, 0, 0), 0) == 6)
    check(countStretches(intArrayOf(), 0) == 0)
    check(countStretches(intArrayOf(5), 3) == 0)
    check(countStretches(intArrayOf(3), 3) == 1)
    check(countStretches(intArrayOf(1, -1, 1, -1), 0) == 4)
    println("018 ok")
}
