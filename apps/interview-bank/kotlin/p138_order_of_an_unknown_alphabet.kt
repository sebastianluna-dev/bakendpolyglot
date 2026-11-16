// 138 · Order of an Unknown Alphabet
package p138

fun alphabetOrder(words: List<String>): String {
    val present = BooleanArray(26)
    for (w in words) for (ch in w) present[ch - 'a'] = true
    val before = Array(26) { BooleanArray(26) } // before[x][y]: x comes before y
    val waiting = IntArray(26) // letters that must come before this one and are not placed yet
    for (i in 0 until words.size - 1) {
        val a = words[i]
        val b = words[i + 1]
        var j = 0
        while (j < a.length && j < b.length && a[j] == b[j]) j++
        if (j == a.length || j == b.length) {
            if (a.length > b.length) return "" // a word sorted before its own prefix
            continue
        }
        val x = a[j] - 'a'
        val y = b[j] - 'a'
        if (!before[x][y]) {
            before[x][y] = true
            waiting[y]++
        }
    }
    val placed = BooleanArray(26)
    val order = StringBuilder()
    while (true) {
        var x = 0
        while (x < 26 && !(present[x] && !placed[x] && waiting[x] == 0)) x++
        if (x == 26) break
        placed[x] = true
        order.append('a' + x)
        for (y in 0 until 26) if (before[x][y]) waiting[y]--
    }
    return if (order.length == present.count { it }) order.toString() else "" // shorter means a cycle
}

fun main() {
    check(alphabetOrder(listOf("cab", "cad", "ab", "ba", "bd")) == "cabd")
    check(alphabetOrder(listOf("ba", "bc", "ad")) == "bacd")
    check(alphabetOrder(listOf("abc", "ab")) == "")
    check(alphabetOrder(listOf("a", "b", "a")) == "")
    check(alphabetOrder(listOf("a", "b", "c", "a")) == "")
    check(alphabetOrder(listOf("zyx")) == "xyz")
    check(alphabetOrder(listOf("ab", "ab")) == "ab")
    check(alphabetOrder(listOf("ab", "abc")) == "abc")
    check(alphabetOrder(listOf("x", "z", "y")) == "xzy")
    println("138 ok")
}
