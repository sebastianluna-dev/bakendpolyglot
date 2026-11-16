// 182 · Hand Out Cookies
package p182

fun contentChildren(greed: IntArray, sizes: IntArray): Int {
    greed.sort()
    sizes.sort()
    var child = 0 // the least greedy child still without a cookie
    for (size in sizes) {
        if (child < greed.size && size >= greed[child]) child++
    }
    return child
}

fun main() {
    check(contentChildren(intArrayOf(5, 2, 8, 2), intArrayOf(3, 1, 6, 9)) == 3)
    check(contentChildren(intArrayOf(1, 3), intArrayOf(3, 1)) == 2)
    check(contentChildren(intArrayOf(5, 2, 8, 2), intArrayOf(3, 1, 6)) == 2)
    check(contentChildren(intArrayOf(4, 4), intArrayOf()) == 0)
    check(contentChildren(intArrayOf(), intArrayOf(1, 2)) == 0)
    check(contentChildren(intArrayOf(10, 20), intArrayOf(1, 2, 3)) == 0)
    check(contentChildren(intArrayOf(2, 2, 2), intArrayOf(2, 2)) == 2)
    check(contentChildren(intArrayOf(1, 2, 3), intArrayOf(3)) == 1)
    println("182 ok")
}
