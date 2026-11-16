// 103 · Depth of a Tree
package p103

class TreeNode(val value: Int, var left: TreeNode? = null, var right: TreeNode? = null)

fun maxDepth(root: TreeNode?): Int {
    if (root == null) return 0
    return 1 + maxOf(maxDepth(root.left), maxDepth(root.right))
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
    check(maxDepth(fromLevelOrder(listOf(5, 2, 9, null, 4, 7, null, null, null, 6))) == 4)
    check(maxDepth(fromLevelOrder(listOf(1, null, 2, null, 3))) == 3)
    check(maxDepth(fromLevelOrder(listOf())) == 0)
    check(maxDepth(fromLevelOrder(listOf(42))) == 1)
    check(maxDepth(fromLevelOrder(listOf(1, 2, 3))) == 2)
    println("103 ok")
}
