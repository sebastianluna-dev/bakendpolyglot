// 028 · Longest Shared Prefix
package p028

fun longestSharedPrefix(words: Array<String>): String {
    if (words.isEmpty()) return ""
    val first = words[0]
    for (i in first.indices) {
        for (word in words) {
            if (i == word.length || word[i] != first[i]) return first.substring(0, i)
        }
    }
    return first
}

fun main() {
    check(longestSharedPrefix(arrayOf("QA76.73", "QA76.76", "QA76.9")) == "QA76.")
    check(longestSharedPrefix(arrayOf("parcel", "pulse", "seat")) == "")
    check(longestSharedPrefix(arrayOf()) == "")
    check(longestSharedPrefix(arrayOf("solo")) == "solo")
    check(longestSharedPrefix(arrayOf("ab", "a")) == "a")
    check(longestSharedPrefix(arrayOf("", "x")) == "")
    check(longestSharedPrefix(arrayOf("hold", "hold")) == "hold")
    println("028 ok")
}
