// 062 · Stretches With a Small Product
package p062

fun countSmallProducts(factors: IntArray, limit: Int): Int {
    if (limit <= 1) return 0 // every product is at least 1
    var left = 0
    var product = 1 // product of factors[left..right]
    var count = 0
    for (right in factors.indices) {
        product *= factors[right]
        while (product >= limit) {
            product /= factors[left]
            left++
        }
        count += right - left + 1 // every stretch that ends at right and starts at left or later
    }
    return count
}

fun main() {
    check(countSmallProducts(intArrayOf(4, 2, 6, 1, 3), 15) == 10)
    check(countSmallProducts(intArrayOf(1, 1, 1), 1) == 0)
    check(countSmallProducts(intArrayOf(1, 1, 1), 2) == 6)
    check(countSmallProducts(intArrayOf(), 10) == 0)
    check(countSmallProducts(intArrayOf(7), 0) == 0)
    check(countSmallProducts(intArrayOf(10, 5, 2), 100) == 5)
    check(countSmallProducts(intArrayOf(1000, 1000), 1000000) == 2)
    println("062 ok")
}
