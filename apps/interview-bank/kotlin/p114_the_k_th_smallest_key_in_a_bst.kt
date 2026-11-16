// 114 · The k-th Smallest Key in a BST
package p114

class TreeNode(val value: Int, var left: TreeNode? = null, var right: TreeNode? = null)

fun kthSmallest(root: TreeNode?, k: Int): Int {
    val stack = ArrayDeque<TreeNode>()
    var node = root
    var remaining = k
    while (true) {
        while (node != null) {
            stack.addLast(node)
            node = node.left
        }
        val smallest = stack.removeLast() // the smallest key not yet counted
        remaining--
        if (remaining == 0) return smallest.value
        node = smallest.right
    }
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
    check(kthSmallest(fromLevelOrder(listOf(20, 10, 30, 5, 15, 25, 40)), 3) == 15)
    check(kthSmallest(fromLevelOrder(listOf(6, 3, 8, 1, 4, null, 9, null, 2)), 2) == 2)
    check(kthSmallest(fromLevelOrder(listOf(6, 3, 8, 1, 4, null, 9, null, 2)), 7) == 9)
    check(kthSmallest(fromLevelOrder(listOf(42)), 1) == 42)
    check(kthSmallest(fromLevelOrder(listOf(1, null, 2, null, 3)), 3) == 3)
    check(kthSmallest(fromLevelOrder(listOf(3, 2, null, 1)), 1) == 1)
    println("114 ok")
}
