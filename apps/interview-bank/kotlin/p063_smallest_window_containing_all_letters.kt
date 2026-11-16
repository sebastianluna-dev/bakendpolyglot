// 063 · Smallest Window Containing All Letters
package p063

fun smallestWindow(text: String, letters: String): String {
    val need = IntArray(128) // copies still wanted, by character code
    for (c in letters) need[c.code]++
    var missing = letters.length
    var bestStart = 0
    var bestLength = text.length + 1
    var left = 0
    for (right in text.indices) {
        val c = text[right].code
        if (need[c] > 0) missing--
        need[c]--
        while (missing == 0) {
            if (right - left + 1 < bestLength) {
                bestStart = left
                bestLength = right - left + 1
            }
            val d = text[left].code
            need[d]++
            if (need[d] > 0) missing++ // a wanted copy just left the window
            left++
        }
    }
    return if (bestLength > text.length) "" else text.substring(bestStart, bestStart + bestLength)
}

fun main() {
    check(smallestWindow("deliverytruck", "tire") == "iveryt")
    check(smallestWindow("banana", "nn") == "nan")
    check(smallestWindow("abba", "ab") == "ab")
    check(smallestWindow("abc", "abd") == "")
    check(smallestWindow("a", "aa") == "")
    check(smallestWindow("Aa", "a") == "a")
    check(smallestWindow("", "a") == "")
    check(smallestWindow("parcel", "parcel") == "parcel")
    println("063 ok")
}
