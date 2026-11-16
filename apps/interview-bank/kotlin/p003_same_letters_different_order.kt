// 003 · Same Letters, Different Order
package p003

fun sameLetters(a: String, b: String): Boolean {
    if (a.length != b.length) return false
    val counts = IntArray(26)
    for (i in a.indices) {
        counts[a[i] - 'a']++
        counts[b[i] - 'a']--
    }
    return counts.all { it == 0 }
}

fun main() {
    check(sameLetters("dusty", "study"))
    check(!sameLetters("aab", "abb"))
    check(!sameLetters("loan", "loans"))
    check(sameLetters("abc", "abc"))
    check(sameLetters("", ""))
    println("003 ok")
}
