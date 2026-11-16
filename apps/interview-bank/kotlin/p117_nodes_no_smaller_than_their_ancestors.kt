// 117 · Nodes No Smaller Than Their Ancestors
package p117

class TreeNode(val value: Int, var left: TreeNode? = null, var right: TreeNode? = null)

fun countGoodNodes(root: TreeNode?): Int = if (root == null) 0 else countFrom(root, root.value)

// highest is the largest value on the path from the root down to node's parent
fun countFrom(node: TreeNode?, highest: Int): Int {
    if (node == null) return 0
    val good = if (node.value >= highest) 1 else 0
    val next = maxOf(highest, node.value)
    return good + countFrom(node.left, next) + countFrom(node.right, next)
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
    check(countGoodNodes(fromLevelOrder(listOf(5, 3, 8, 6, 2, 5, 9))) == 4)
    check(countGoodNodes(fromLevelOrder(listOf(2, 2, 2))) == 3)
    check(countGoodNodes(fromLevelOrder(listOf())) == 0)
    check(countGoodNodes(fromLevelOrder(listOf(7))) == 1)
    check(countGoodNodes(fromLevelOrder(listOf(9, 8, null, 7))) == 1)
    check(countGoodNodes(fromLevelOrder(listOf(-5, -10, -1))) == 2)
    println("117 ok")
}
