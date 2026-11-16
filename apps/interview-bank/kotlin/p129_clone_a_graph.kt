// 129 · Clone a Graph
package p129

class GraphNode(val value: Int) {
    val neighbors = mutableListOf<GraphNode>()
}

fun cloneGraph(node: GraphNode?): GraphNode? {
    if (node == null) return null
    val copyOf = hashMapOf(node to GraphNode(node.value)) // original -> copy
    val queue = ArrayDeque<GraphNode>()
    queue.addLast(node)
    while (queue.isNotEmpty()) {
        val current = queue.removeFirst()
        for (nb in current.neighbors) {
            if (nb !in copyOf) {
                copyOf[nb] = GraphNode(nb.value)
                queue.addLast(nb)
            }
            copyOf.getValue(current).neighbors.add(copyOf.getValue(nb))
        }
    }
    return copyOf.getValue(node)
}

// Node i has value i and the neighbours adjacency[i]; returns node 0.
fun build(adjacency: List<List<Int>>): GraphNode? {
    val nodes = List(adjacency.size) { GraphNode(it) }
    for ((i, neighbors) in adjacency.withIndex()) neighbors.mapTo(nodes[i].neighbors) { nodes[it] }
    return nodes.firstOrNull()
}

fun allNodes(node: GraphNode?): Set<GraphNode> {
    if (node == null) return emptySet()
    val found = hashSetOf(node)
    val queue = ArrayDeque<GraphNode>()
    queue.addLast(node)
    while (queue.isNotEmpty()) {
        for (nb in queue.removeFirst().neighbors) {
            if (found.add(nb)) queue.addLast(nb)
        }
    }
    return found
}

fun toAdjacency(node: GraphNode?): List<List<Int>> {
    val byValue = allNodes(node).associateBy { it.value }
    return List(byValue.size) { i -> byValue.getValue(i).neighbors.map { it.value } }
}

fun checkClone(adjacency: List<List<Int>>) {
    val original = build(adjacency)
    val copy = cloneGraph(original)
    check(toAdjacency(copy) == adjacency)
    check(allNodes(original).intersect(allNodes(copy)).isEmpty())
}

fun main() {
    checkClone(listOf(listOf(1, 3), listOf(0, 2), listOf(1, 3), listOf(0, 2)))
    checkClone(listOf(listOf()))
    checkClone(listOf())
    checkClone(listOf(listOf(1), listOf(0)))
    checkClone(listOf(listOf(1, 2), listOf(0, 2), listOf(0, 1, 3), listOf(2)))
    println("129 ok")
}
