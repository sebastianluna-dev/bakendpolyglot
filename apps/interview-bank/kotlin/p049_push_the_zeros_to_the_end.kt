// 049 · Push the Zeros to the End
package p049

fun pushZerosToEnd(slots: IntArray) {
    var write = 0 // slots[0 until write] holds the parcels seen so far, in order
    for (read in slots.indices) {
        if (slots[read] != 0) {
            val tmp = slots[write]
            slots[write] = slots[read]
            slots[read] = tmp
            write++
        }
    }
}

fun pushed(slots: IntArray): IntArray = slots.also { pushZerosToEnd(it) }

fun main() {
    check(pushed(intArrayOf(0, 41, 0, 0, 17, 23)).contentEquals(intArrayOf(41, 17, 23, 0, 0, 0)))
    check(pushed(intArrayOf(5, 0, 3)).contentEquals(intArrayOf(5, 3, 0)))
    check(pushed(intArrayOf()).isEmpty())
    check(pushed(intArrayOf(0, 0)).contentEquals(intArrayOf(0, 0)))
    check(pushed(intArrayOf(1, 2)).contentEquals(intArrayOf(1, 2)))
    check(pushed(intArrayOf(0, 0, 1)).contentEquals(intArrayOf(1, 0, 0)))
    println("049 ok")
}
