// 108 · Longest Path Between Two Nodes
package p108

class TreeNode(val value: Int, var left: TreeNode? = null, var right: TreeNode? = null)

fun longestPath(root: TreeNode?): Int {
    var best = 0
    // Returns the depth in nodes, which is also the number of edges from the parent down.
    fun depth(node: TreeNode?): Int {
        if (node == null) return 0
        val left = depth(node.left)
        val right = depth(node.right)
        best = maxOf(best, left + right)
        return 1 + maxOf(left, right)
    }
    depth(root)
    return best
}

fun fromLevelOrder(values: List<Int?>): TreeNode? {
    val first = values.firstOrNull() ?: return null
    val root = TreeNode(first)
    val queue = ArrayDeque(listOf(root))
    for (i in 1 until values.size step 2) {
        val node = queue.removeFirst()
        val left = values[i]
        val right = values.getOrNull(i + 1)
        if (left != null) node.left = TreeNode(left).also { queue.addLast(it) }
        if (right != null) node.right = TreeNode(right).also { queue.addLast(it) }
    }
    return root
}

fun main() {
    check(longestPath(fromLevelOrder(listOf(8, 4, 11, 2, 6, null, null, 1))) == 4)
    check(longestPath(fromLevelOrder(listOf(1, 2, null, 3, 4, 5, null, null, 6, 7, null, null, 8))) == 6)
    check(longestPath(fromLevelOrder(listOf())) == 0)
    check(longestPath(fromLevelOrder(listOf(9))) == 0)
    check(longestPath(fromLevelOrder(listOf(1, 2))) == 1)
    println("108 ok")
}
