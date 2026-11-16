// 172 · Can the Text Be Split Into Words?
package p172

fun canSplit(text: String, words: List<String>): Boolean {
    val dictionary = words.toHashSet()
    val longest = words.maxOfOrNull { it.length } ?: 0
    val splits = BooleanArray(text.length + 1) // splits[i]: the first i letters split into words
    splits[0] = true
    for (end in 1..text.length) {
        for (len in 1..minOf(end, longest)) {
            if (splits[end - len] && text.substring(end - len, end) in dictionary) {
                splits[end] = true
                break
            }
        }
    }
    return splits[text.length]
}

fun main() {
    check(canSplit("bookseller", listOf("books", "book", "seller")))
    check(!canSplit("trucking", listOf("truck", "king", "in")))
    check(canSplit("", listOf("a")))
    check(!canSplit("abcd", listOf()))
    check(canSplit("seatseat", listOf("seat")))
    check(canSplit("aaaaaaa", listOf("aaaa", "aaa")))
    check(!canSplit("aaaaaaaaab", listOf("a", "aa")))
    println("172 ok")
}
