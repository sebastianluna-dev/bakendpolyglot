// 194 · Parse a Query String
package p194

import java.io.ByteArrayOutputStream

fun parseQuery(query: String): Map<String, List<String>> {
    val params = HashMap<String, MutableList<String>>()
    for (piece in query.split("&")) {
        val parts = piece.split("=", limit = 2) // split at the first = only
        val key = decode(parts[0])
        if (key.isEmpty()) continue
        val value = if (parts.size == 2) decode(parts[1]) else ""
        params.getOrPut(key) { mutableListOf() }.add(value)
    }
    return params
}

fun decode(s: String): String {
    val bytes = ByteArrayOutputStream()
    var i = 0
    while (i < s.length) {
        val c = s[i]
        if (c == '+') {
            bytes.write(' '.code)
        } else if (c == '%' && i + 2 < s.length && hexValue(s[i + 1]) >= 0 && hexValue(s[i + 2]) >= 0) {
            bytes.write(hexValue(s[i + 1]) * 16 + hexValue(s[i + 2]))
            i += 2
        } else {
            bytes.write(c.code) // a malformed % stays as it is
        }
        i++
    }
    return bytes.toByteArray().decodeToString() // %XX escapes are bytes; several can make one character
}

fun hexValue(c: Char): Int = when (c) {
    in '0'..'9' -> c - '0'
    in 'a'..'f' -> c - 'a' + 10
    in 'A'..'F' -> c - 'A' + 10
    else -> -1
}

fun main() {
    check(parseQuery("genre=sci-fi&sort=title&genre=history") == mapOf("genre" to listOf("sci-fi", "history"), "sort" to listOf("title")))
    check(
        parseQuery("q=war+%26+peace&note=50%25&empty=&flag") ==
            mapOf("q" to listOf("war & peace"), "note" to listOf("50%"), "empty" to listOf(""), "flag" to listOf("")),
    )
    check(parseQuery("") == mapOf<String, List<String>>())
    check(parseQuery("a=1&&b=2&") == mapOf("a" to listOf("1"), "b" to listOf("2")))
    check(parseQuery("=5&k=v") == mapOf("k" to listOf("v")))
    check(parseQuery("expr=a=b") == mapOf("expr" to listOf("a=b")))
    check(parseQuery("name=Jos%C3%A9&price=%E2%82%AC5") == mapOf("name" to listOf("José"), "price" to listOf("€5")))
    check(parseQuery("bad=%zz%4&tail=%") == mapOf("bad" to listOf("%zz%4"), "tail" to listOf("%")))
    check(parseQuery("sort%5Bby%5D=title&path=%2fa%2F") == mapOf("sort[by]" to listOf("title"), "path" to listOf("/a/")))
    check(parseQuery("plus=%2B+") == mapOf("plus" to listOf("+ ")))
    println("194 ok")
}
