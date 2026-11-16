// 133 · Count Network Segments
package p133

fun countSegments(n: Int, links: Array<IntArray>): Int {
    val parent = IntArray(n) { it }
    val size = IntArray(n) { 1 }
    var segments = n
    for ((a, b) in links) {
        var ra = find(parent, a)
        var rb = find(parent, b)
        if (ra == rb) continue
        if (size[ra] < size[rb]) ra = rb.also { rb = ra }
        parent[rb] = ra // the smaller tree goes under the larger one
        size[ra] += size[rb]
        segments--
    }
    return segments
}

fun find(parent: IntArray, x: Int): Int {
    var node = x
    while (parent[node] != node) {
        parent[node] = parent[parent[node]] // path halving: point node at its grandparent
        node = parent[node]
    }
    return node
}

fun main() {
    check(countSegments(5, arrayOf(intArrayOf(0, 1), intArrayOf(1, 2), intArrayOf(3, 4))) == 2)
    check(countSegments(6, arrayOf(intArrayOf(0, 1), intArrayOf(2, 3), intArrayOf(1, 0))) == 4)
    check(countSegments(1, arrayOf()) == 1)
    check(countSegments(3, arrayOf()) == 3)
    check(countSegments(3, arrayOf(intArrayOf(0, 1), intArrayOf(1, 2), intArrayOf(2, 0))) == 1)
    check(countSegments(2, arrayOf(intArrayOf(1, 1))) == 2)
    check(
        countSegments(
            6,
            arrayOf(intArrayOf(0, 1), intArrayOf(2, 3), intArrayOf(4, 5), intArrayOf(1, 3), intArrayOf(5, 0)),
        ) == 1,
    )
    println("133 ok")
}
