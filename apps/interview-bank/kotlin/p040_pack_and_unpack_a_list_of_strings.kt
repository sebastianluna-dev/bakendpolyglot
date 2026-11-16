// 040 · Pack and Unpack a List of Strings
package p040

fun pack(words: List<String>): String = buildString {
    for (w in words) append(w.length).append('#').append(w)
}

fun unpack(packed: String): List<String> {
    val words = mutableListOf<String>()
    var i = 0
    while (i < packed.length) {
        var length = 0
        while (packed[i] != '#') {
            length = length * 10 + (packed[i] - '0')
            i++
        }
        i++ // skip the '#'
        words.add(packed.substring(i, i + length))
        i += length
    }
    return words
}

fun main() {
    val cases = listOf(
        listOf("truck 7", "", "door#2") to "7#truck 70#6#door#2",
        listOf("parcel-12345") to "12#parcel-12345",
        listOf<String>() to "",
        listOf("") to "0#",
        listOf("#", "12#", "a b", "3#x") to "1##3#12#3#a b3#3#x",
    )
    for ((words, packed) in cases) {
        check(pack(words) == packed)
        check(unpack(packed) == words)
    }
    println("040 ok")
}
