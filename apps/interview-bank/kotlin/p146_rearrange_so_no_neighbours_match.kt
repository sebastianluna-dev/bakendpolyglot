// 146 · Rearrange So No Neighbours Match
package p146

import java.util.PriorityQueue

fun rearrange(s: String): String {
    val counts = IntArray(26)
    for (ch in s) counts[ch - 'a']++
    // (copies left, letter): most copies first, then the earlier letter
    val heap = PriorityQueue(compareByDescending<Pair<Int, Char>> { it.first }.thenBy { it.second })
    for (i in 0 until 26) if (counts[i] > 0) heap.add(counts[i] to 'a' + i)
    val sb = StringBuilder()
    var held = 0 to ' ' // the letter just placed, kept out of the heap for one step
    while (heap.isNotEmpty()) {
        val (c, ch) = heap.poll()
        sb.append(ch)
        if (held.first > 0) heap.add(held)
        held = c - 1 to ch
    }
    return if (sb.length == s.length) sb.toString() else ""
}

fun main() {
    check(rearrange("aab") == "aba")
    check(rearrange("aaab") == "")
    check(rearrange("vvvlo") == "vlvov")
    check(rearrange("aabbcc") == "abcabc")
    check(rearrange("aaabbc") == "ababac")
    check(rearrange("a") == "a")
    check(rearrange("zz") == "")
    println("146 ok")
}
