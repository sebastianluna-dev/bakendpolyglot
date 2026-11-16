// 070 · Remove Adjacent Pairs
package p070

fun removePairs(s: String): String {
    val kept = StringBuilder() // used as a stack: the result so far
    for (ch in s) {
        if (kept.isNotEmpty() && kept.last() == ch) kept.setLength(kept.length - 1)
        else kept.append(ch)
    }
    return kept.toString()
}

fun main() {
    check(removePairs("abccbd") == "ad")
    check(removePairs("aabb") == "")
    check(removePairs("") == "")
    check(removePairs("abc") == "abc")
    check(removePairs("aaa") == "a")
    check(removePairs("abba") == "")
    check(removePairs("mississippi") == "m")
    println("070 ok")
}
