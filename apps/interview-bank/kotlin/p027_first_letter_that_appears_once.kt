// 027 · First Letter That Appears Once
package p027

fun firstUniqueIndex(word: String): Int {
    val counts = IntArray(26)
    for (ch in word) counts[ch - 'a']++
    for (i in word.indices) {
        if (counts[word[i] - 'a'] == 1) return i
    }
    return -1
}

fun main() {
    check(firstUniqueIndex("shelfwise") == 1)
    check(firstUniqueIndex("noon") == -1)
    check(firstUniqueIndex("") == -1)
    check(firstUniqueIndex("z") == 0)
    check(firstUniqueIndex("aabbc") == 4)
    check(firstUniqueIndex("pear") == 0)
    println("027 ok")
}
