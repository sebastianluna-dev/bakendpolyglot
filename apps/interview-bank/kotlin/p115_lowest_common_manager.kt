// 115 · Lowest Common Manager
package p115

class TreeNode(val value: Int, var left: TreeNode? = null, var right: TreeNode? = null)

fun lowestCommonManager(root: TreeNode?, a: Int, b: Int): Int = lowest(root, a, b)!!.value

// returns the answer if both ids are below node, else whichever of the two is below it, else null
fun lowest(node: TreeNode?, a: Int, b: Int): TreeNode? {
    if (node == null || node.value == a || node.value == b) return node
    val left = lowest(node.left, a, b)
    val right = lowest(node.right, a, b)
    if (left != null && right != null) return node
    return left ?: right
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
    val chart = listOf(1, 2, 3, 4, 5, 6, 7, null, null, 8, 9)
    check(lowestCommonManager(fromLevelOrder(chart), 8, 4) == 2)
    check(lowestCommonManager(fromLevelOrder(chart), 9, 7) == 1)
    check(lowestCommonManager(fromLevelOrder(chart), 5, 9) == 5)
    check(lowestCommonManager(fromLevelOrder(chart), 6, 6) == 6)
    check(lowestCommonManager(fromLevelOrder(chart), 8, 9) == 5)
    check(lowestCommonManager(fromLevelOrder(listOf(1, 2)), 2, 1) == 1)
    println("115 ok")
}
