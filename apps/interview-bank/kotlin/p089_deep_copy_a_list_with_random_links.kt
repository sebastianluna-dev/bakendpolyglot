// 089 · Deep-Copy a List With Random Links
package p089

class ListNode(val value: Int, var next: ListNode? = null) {
    var random: ListNode? = null
}

fun deepCopy(head: ListNode?): ListNode? {
    val copyOf = HashMap<ListNode, ListNode>() // original node -> its copy
    for (node in generateSequence(head) { it.next }) copyOf[node] = ListNode(node.value)
    for (node in generateSequence(head) { it.next }) {
        val copy = copyOf.getValue(node)
        copy.next = node.next?.let { copyOf[it] }
        copy.random = node.random?.let { copyOf[it] }
    }
    return head?.let { copyOf[it] }
}

// Each pair is (value, index of the random target), with -1 for no target.
fun fromPairs(pairs: List<Pair<Int, Int>>): ListNode? {
    val nodes = pairs.map { ListNode(it.first) }
    for ((i, pair) in pairs.withIndex()) {
        nodes[i].next = nodes.getOrNull(i + 1)
        if (pair.second >= 0) nodes[i].random = nodes[pair.second]
    }
    return nodes.firstOrNull()
}

fun toPairs(head: ListNode?): List<Pair<Int, Int>> {
    val nodes = generateSequence(head) { it.next }.toList()
    val index = nodes.withIndex().associate { (i, node) -> node to i }
    return nodes.map { node -> node.value to (node.random?.let { index.getValue(it) } ?: -1) }
}

fun sharesNode(a: ListNode?, b: ListNode?): Boolean {
    val seen = generateSequence(a) { it.next }.toHashSet()
    return generateSequence(b) { it.next }.any { it in seen || it.random in seen }
}

fun checkCopy(pairs: List<Pair<Int, Int>>) {
    val original = fromPairs(pairs)
    val copy = deepCopy(original)
    check(toPairs(copy) == pairs)
    check(toPairs(original) == pairs)
    check(!sharesNode(original, copy))
}

fun main() {
    checkCopy(listOf(4 to 2, 9 to 0, 6 to -1))
    checkCopy(listOf(1 to 1, 2 to 1))
    checkCopy(listOf())
    checkCopy(listOf(5 to 0))
    checkCopy(listOf(7 to 1, 7 to 0))
    checkCopy(listOf(3 to -1, 8 to -1, 2 to -1))
    println("089 ok")
}
