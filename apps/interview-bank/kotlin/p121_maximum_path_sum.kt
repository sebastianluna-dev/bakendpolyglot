// 121 · Maximum Path Sum
package p121

class TreeNode(val value: Int, var left: TreeNode? = null, var right: TreeNode? = null)

fun maxPathSum(root: TreeNode): Int {
    var best = root.value
    // returns the best sum of a path that starts at node and goes down, or 0 for no node
    fun gain(node: TreeNode?): Int {
        if (node == null) return 0
        val left = maxOf(0, gain(node.left)) // a negative branch is better left out
        val right = maxOf(0, gain(node.right))
        best = maxOf(best, node.value + left + right)
        return node.value + maxOf(left, right)
    }
    gain(root)
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
    check(maxPathSum(fromLevelOrder(listOf(6, -8, 3, null, 9, -1, 4))!!) == 14)
    check(maxPathSum(fromLevelOrder(listOf(-3, -1, -2))!!) == -1)
    check(maxPathSum(fromLevelOrder(listOf(-5))!!) == -5)
    check(maxPathSum(fromLevelOrder(listOf(-10, 4, -1, 6, 7))!!) == 17)
    check(maxPathSum(fromLevelOrder(listOf(2, -1, null, 5))!!) == 6)
    check(maxPathSum(fromLevelOrder(listOf(1, -2, 3, 4, 5, -6, 2))!!) == 9)
    println("121 ok")
}
