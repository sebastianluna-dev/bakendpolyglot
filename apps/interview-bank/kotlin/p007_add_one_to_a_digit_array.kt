// 007 · Add One to a Digit Array
package p007

fun addOne(digits: IntArray): IntArray {
    val result = digits.copyOf()
    for (i in result.indices.reversed()) {
        if (result[i] < 9) {
            result[i]++
            return result
        }
        result[i] = 0
    }
    return intArrayOf(1) + result // every digit was 9
}

fun main() {
    val input = intArrayOf(1, 2, 9)
    check(addOne(input).contentEquals(intArrayOf(1, 3, 0)))
    check(input.contentEquals(intArrayOf(1, 2, 9)))
    check(addOne(intArrayOf(9, 9, 9)).contentEquals(intArrayOf(1, 0, 0, 0)))
    check(addOne(intArrayOf(4, 5)).contentEquals(intArrayOf(4, 6)))
    check(addOne(intArrayOf(0)).contentEquals(intArrayOf(1)))
    check(addOne(IntArray(20) { 9 }).contentEquals(intArrayOf(1) + IntArray(20)))
    println("007 ok")
}
