// 052 · Compare Strings With Backspaces
package p052

fun sameAfterBackspaces(a: String, b: String): Boolean {
    var i = a.length - 1
    var j = b.length - 1
    while (i >= 0 || j >= 0) {
        i = lastKept(a, i)
        j = lastKept(b, j)
        if (i < 0 || j < 0) return i < 0 && j < 0
        if (a[i] != b[j]) return false
        i--
        j--
    }
    return true
}

// Index of the last character at or before start that no later '#' deletes, or -1.
private fun lastKept(s: String, start: Int): Int {
    var i = start
    var skip = 0
    while (i >= 0) {
        if (s[i] == '#') skip++
        else if (skip > 0) skip--
        else break
        i--
    }
    return i
}

fun main() {
    check(sameAfterBackspaces("A1#2", "A2"))
    check(!sameAfterBackspaces("B7##C", "C#B"))
    check(sameAfterBackspaces("##", ""))
    check(sameAfterBackspaces("ab##", "c#d#"))
    check(sameAfterBackspaces("x#y", "y"))
    check(!sameAfterBackspaces("abc", "ab"))
    check(!sameAfterBackspaces("a", "ba"))
    check(sameAfterBackspaces("a#b#c", "#c"))
    println("052 ok")
}
