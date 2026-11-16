// 034 · Find a Needle in a Haystack
package p034

fun findNeedle(haystack: String, needle: String): Int {
    for (start in 0..haystack.length - needle.length) {
        var k = 0
        while (k < needle.length && haystack[start + k] == needle[k]) k++
        if (k == needle.length) return start
    }
    return -1
}

fun main() {
    check(findNeedle("hold-14-seat-14", "14") == 5)
    check(findNeedle("parcel", "pulse") == -1)
    check(findNeedle("abc", "c") == 2)
    check(findNeedle("aaab", "ab") == 2)
    check(findNeedle("abc", "") == 0)
    check(findNeedle("", "") == 0)
    check(findNeedle("", "a") == -1)
    check(findNeedle("ab", "abc") == -1)
    println("034 ok")
}
