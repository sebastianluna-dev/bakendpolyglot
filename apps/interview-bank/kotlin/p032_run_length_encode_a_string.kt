// 032 · Run-Length Encode a String
package p032

fun runLengthEncode(s: String): String = buildString {
    var i = 0
    while (i < s.length) {
        var j = i
        while (j < s.length && s[j] == s[i]) j++
        append(s[i]).append(j - i)
        i = j
    }
}

fun main() {
    check(runLengthEncode("aaabccdddd") == "a3b1c2d4")
    check(runLengthEncode("abc") == "a1b1c1")
    check(runLengthEncode("") == "")
    check(runLengthEncode("z".repeat(12)) == "z12")
    check(runLengthEncode("aAaa") == "a1A1a2")
    check(runLengthEncode("abb") == "a1b2")
    println("032 ok")
}
