// 055 · Longest Stretch Without a Repeat
package p055

fun longestWithoutRepeat(s: String): Int {
    val last = HashMap<Char, Int>() // character -> index where it was last seen
    var start = 0
    var best = 0
    for ((i, ch) in s.withIndex()) {
        val prev = last[ch]
        if (prev != null && prev >= start) start = prev + 1 // only a repeat inside the window counts
        last[ch] = i
        best = maxOf(best, i - start + 1)
    }
    return best
}

fun main() {
    check(longestWithoutRepeat("shelfwise") == 7)
    check(longestWithoutRepeat("aaaa") == 1)
    check(longestWithoutRepeat("") == 0)
    check(longestWithoutRepeat("abba") == 2)
    check(longestWithoutRepeat("dvdf") == 3)
    check(longestWithoutRepeat("A-1 a-2") == 5)
    println("055 ok")
}
