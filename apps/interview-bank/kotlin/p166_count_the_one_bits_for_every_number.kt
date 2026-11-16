// 166 · Count the One Bits for Every Number
package p166

fun oneBitCounts(n: Int): IntArray {
    val bits = IntArray(n + 1)
    for (i in 1..n) {
        bits[i] = bits[i shr 1] + (i and 1) // i without its last bit, plus that bit
    }
    return bits
}

fun main() {
    check(oneBitCounts(5).contentEquals(intArrayOf(0, 1, 1, 2, 1, 2)))
    check(oneBitCounts(0).contentEquals(intArrayOf(0)))
    check(oneBitCounts(1).contentEquals(intArrayOf(0, 1)))
    check(oneBitCounts(8).contentEquals(intArrayOf(0, 1, 1, 2, 1, 2, 2, 3, 1)))
    check(oneBitCounts(1023)[1023] == 10)
    check(oneBitCounts(100000)[65535] == 16)
    println("166 ok")
}
