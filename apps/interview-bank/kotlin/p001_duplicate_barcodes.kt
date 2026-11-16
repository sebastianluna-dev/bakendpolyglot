// 001 · Duplicate Barcodes
package p001

fun hasDuplicate(barcodes: Array<String>): Boolean {
    val seen = HashSet<String>()
    for (code in barcodes) {
        if (!seen.add(code)) return true // add returns false when the code is already there
    }
    return false
}

fun main() {
    check(hasDuplicate(arrayOf("SW-1042", "SW-0007", "SW-2210", "SW-0007")))
    check(!hasDuplicate(arrayOf("SW-1042", "SW-0007", "SW-2210")))
    check(!hasDuplicate(arrayOf("SW-0007", "sw-0007")))
    check(!hasDuplicate(arrayOf("SW-1042")))
    check(!hasDuplicate(arrayOf()))
    println("001 ok")
}
