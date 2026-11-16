// 123 · Is There a Route?
package p123

fun hasRoute(n: Int, edges: Array<IntArray>, source: Int, target: Int): Boolean {
    val graph = List(n) { mutableListOf<Int>() }
    for ((a, b) in edges) {
        graph[a].add(b)
        graph[b].add(a)
    }
    val seen = BooleanArray(n)
    seen[source] = true
    val queue = ArrayDeque<Int>()
    queue.addLast(source)
    while (queue.isNotEmpty()) {
        val node = queue.removeFirst()
        if (node == target) return true
        for (next in graph[node]) {
            if (seen[next]) continue
            seen[next] = true // mark when queued, so no node is queued twice
            queue.addLast(next)
        }
    }
    return false
}

fun main() {
    val roads = arrayOf(intArrayOf(0, 1), intArrayOf(1, 3), intArrayOf(3, 2), intArrayOf(4, 5))
    check(hasRoute(6, roads, 0, 2))
    check(!hasRoute(6, roads, 0, 5))
    check(hasRoute(1, arrayOf(), 0, 0))
    check(!hasRoute(3, arrayOf(), 0, 2))
    check(hasRoute(2, arrayOf(intArrayOf(1, 0)), 0, 1))
    check(hasRoute(4, arrayOf(intArrayOf(0, 1), intArrayOf(1, 0), intArrayOf(1, 2), intArrayOf(2, 0), intArrayOf(2, 3)), 3, 0))
    println("123 ok")
}
