// 109 · A Root-to-Leaf Path With a Given Sum
package p109

class TreeNode(val value: Int, var left: TreeNode? = null, var right: TreeNode? = null)

fun hasPathSum(root: TreeNode?, target: Int): Boolean {
    if (root == null) return false
    if (root.left == null && root.right == null) return root.value == target
    val rest = target - root.value
    return hasPathSum(root.left, rest) || hasPathSum(root.right, rest)
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
    check(hasPathSum(fromLevelOrder(listOf(5, 3, 8, 1, null, null, 4)), 9))
    check(!hasPathSum(fromLevelOrder(listOf(2, 7)), 2))
    check(!hasPathSum(fromLevelOrder(listOf()), 0))
    check(hasPathSum(fromLevelOrder(listOf(5, 3, 8, 1, null, null, 4)), 17))
    check(!hasPathSum(fromLevelOrder(listOf(5, 3, 8, 1, null, null, 4)), 8))
    check(hasPathSum(fromLevelOrder(listOf(-3, 1, -2)), -5))
    println("109 ok")
}
