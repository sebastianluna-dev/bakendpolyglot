// 014 · Group Words by Their Letters
package p014

fun groupByLetters(words: List<String>): List<List<String>> {
    val groupOf = HashMap<String, Int>() // sorted letters -> index in groups
    val groups = mutableListOf<MutableList<String>>()
    for (word in words) {
        val key = word.toCharArray().sorted().joinToString("")
        val g = groupOf.getOrPut(key) {
            groups.add(mutableListOf())
            groups.size - 1
        }
        groups[g].add(word)
    }
    return groups
}

fun main() {
    check(
        groupByLetters(listOf("listen", "stone", "silent", "notes", "enlist", "tones", "tab")) ==
            listOf(listOf("listen", "silent", "enlist"), listOf("stone", "notes", "tones"), listOf("tab")),
    )
    check(groupByLetters(listOf("loop", "pool", "polo", "lop")) == listOf(listOf("loop", "pool", "polo"), listOf("lop")))
    check(groupByLetters(listOf()).isEmpty())
    check(groupByLetters(listOf("ab", "ba", "ab")) == listOf(listOf("ab", "ba", "ab")))
    check(groupByLetters(listOf("", "a", "")) == listOf(listOf("", ""), listOf("a")))
    println("014 ok")
}
