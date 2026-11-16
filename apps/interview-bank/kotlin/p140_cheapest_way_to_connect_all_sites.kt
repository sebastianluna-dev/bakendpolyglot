// 140 · Cheapest Way to Connect All Sites
package p140

fun cheapestConnection(n: Int, cables: Array<IntArray>): Int {
    val parent = IntArray(n) { it }
    val size = IntArray(n) { 1 }
    var total = 0
    var used = 0
    for ((a, b, cost) in cables.sortedBy { it[2] }) {
        var ra = find(parent, a)
        var rb = find(parent, b)
        if (ra == rb) continue // both ends already connected: this cable would close a loop
        if (size[ra] < size[rb]) ra = rb.also { rb = ra }
        parent[rb] = ra
        size[ra] += size[rb]
        total += cost
        used++
    }
    return if (used == n - 1) total else -1
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
    check(
        cheapestConnection(
            4,
            arrayOf(intArrayOf(0, 1, 3), intArrayOf(1, 2, 1), intArrayOf(2, 3, 4), intArrayOf(0, 2, 2), intArrayOf(1, 3, 5)),
        ) == 7,
    )
    check(cheapestConnection(3, arrayOf(intArrayOf(0, 1, 5))) == -1)
    check(cheapestConnection(1, arrayOf()) == 0)
    check(cheapestConnection(2, arrayOf()) == -1)
    check(cheapestConnection(2, arrayOf(intArrayOf(0, 1, 9), intArrayOf(1, 0, 4), intArrayOf(0, 0, 1))) == 4)
    check(
        cheapestConnection(
            5,
            arrayOf(
                intArrayOf(0, 1, 2), intArrayOf(1, 2, 2), intArrayOf(2, 0, 2),
                intArrayOf(3, 4, 1), intArrayOf(2, 3, 7), intArrayOf(0, 4, 8),
            ),
        ) == 12,
    )
    println("140 ok")
}
