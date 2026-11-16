// 107 · Is the Tree Height-Balanced?
package p107

import kotlin.math.abs

class TreeNode(val value: Int, var left: TreeNode? = null, var right: TreeNode? = null)

fun isBalanced(root: TreeNode?): Boolean = balancedHeight(root) != -1

// The height of the subtree, or -1 as soon as any subtree in it is unbalanced.
fun balancedHeight(node: TreeNode?): Int {
    if (node == null) return 0
    val left = balancedHeight(node.left)
    if (left == -1) return -1
    val right = balancedHeight(node.right)
    if (right == -1 || abs(left - right) > 1) return -1
    return 1 + maxOf(left, right)
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
    check(isBalanced(fromLevelOrder(listOf(10, 5, 15, null, null, 12, 20))))
    check(!isBalanced(fromLevelOrder(listOf(10, 5, null, 3, null, 1))))
    check(isBalanced(fromLevelOrder(listOf())))
    check(!isBalanced(fromLevelOrder(listOf(1, 2, 3, 4, null, null, 5, 6, null, null, 7))))
    check(isBalanced(fromLevelOrder(listOf(1, 2, 3, 4))))
    check(isBalanced(fromLevelOrder(listOf(1, 2))))
    println("107 ok")
}
