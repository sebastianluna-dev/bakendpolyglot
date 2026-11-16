// 137 · Change One Letter at a Time
package p137

fun shortestChain(start: String, target: String, words: List<String>): Int {
    val dictionary = words.toHashSet()
    val length = hashMapOf(start to 1) // words in the shortest chain that ends here
    val queue = ArrayDeque(listOf(start))
    while (queue.isNotEmpty()) {
        val word = queue.removeFirst()
        if (word == target) return length.getValue(word)
        val chars = word.toCharArray()
        for (i in chars.indices) {
            val original = chars[i]
            for (c in 'a'..'z') {
                chars[i] = c
                val next = String(chars)
                if (next in dictionary && next !in length) {
                    length[next] = length.getValue(word) + 1
                    queue.addLast(next)
                }
            }
            chars[i] = original
        }
    }
    return 0
}

fun main() {
    check(shortestChain("cold", "warm", listOf("cord", "card", "ward", "warm", "worm", "word", "wore")) == 5)
    check(shortestChain("lead", "gold", listOf("load", "goad")) == 0)
    check(shortestChain("cat", "cot", listOf("cot")) == 2)
    check(shortestChain("cat", "dog", listOf()) == 0)
    check(shortestChain("hit", "hot", listOf("hot", "hot", "hit")) == 2)
    check(shortestChain("mine", "bond", listOf("mind", "bind", "bond", "mend")) == 4)
    check(shortestChain("a", "c", listOf("b", "c")) == 2)
    println("137 ok")
}
