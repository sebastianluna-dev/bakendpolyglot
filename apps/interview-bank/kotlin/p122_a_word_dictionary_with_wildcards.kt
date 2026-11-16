// 122 · A Word Dictionary With Wildcards
package p122

class TrieNode {
    val children = arrayOfNulls<TrieNode>(26)
    var isWord = false
}

class WordDictionary {
    private val root = TrieNode()

    fun addWord(word: String) {
        var node = root
        for (ch in word) {
            val i = ch - 'a'
            node = node.children[i] ?: TrieNode().also { node.children[i] = it }
        }
        node.isWord = true
    }

    fun search(pattern: String): Boolean = match(root, pattern, 0)

    private fun match(node: TrieNode, pattern: String, i: Int): Boolean {
        if (i == pattern.length) return node.isWord
        if (pattern[i] == '.') {
            return node.children.any { child -> child != null && match(child, pattern, i + 1) }
        }
        val next = node.children[pattern[i] - 'a']
        return next != null && match(next, pattern, i + 1)
    }
}

fun main() {
    val d = WordDictionary()
    d.addWord("loan")
    d.addWord("lean")
    check(d.search("l.an"))
    check(!d.search("lo"))
    check(d.search("l..."))
    d.addWord("load")
    check(d.search("loan"))
    check(d.search("lo.d"))
    check(!d.search("le.d"))
    check(!d.search("loans"))
    check(d.search("...."))
    check(!d.search("..."))
    check(!d.search("x..."))

    val e = WordDictionary()
    check(!e.search("."))
    e.addWord("a")
    check(e.search("."))
    check(e.search("a"))
    check(!e.search("b"))
    e.addWord("a")
    check(e.search("a"))
    println("122 ok")
}
