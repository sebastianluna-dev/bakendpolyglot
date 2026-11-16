// 177 · Edit Distance
package p177

fun editDistance(source: String, target: String): Int {
    val m = target.length
    // cur[j]: fewest edits turning the first i letters of source into the first j of target; prev holds row i - 1
    var prev = IntArray(m + 1) { it }
    for (i in 1..source.length) {
        val cur = IntArray(m + 1)
        cur[0] = i
        for (j in 1..m) {
            cur[j] = if (source[i - 1] == target[j - 1]) {
                prev[j - 1]
            } else {
                1 + minOf(prev[j], cur[j - 1], prev[j - 1]) // delete, insert, replace
            }
        }
        prev = cur
    }
    return prev[m]
}

fun main() {
    check(editDistance("shelf", "shelves") == 3)
    check(editDistance("loan", "lone") == 2)
    check(editDistance("", "abc") == 3)
    check(editDistance("abc", "") == 3)
    check(editDistance("seat", "seat") == 0)
    check(editDistance("parcel", "pencil") == 3)
    check(editDistance("trucks", "truck") == 1)
    check(editDistance("abc", "cab") == 2)
    check(editDistance("booking", "looking") == 1)
    println("177 ok")
}
