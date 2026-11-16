// 110 · Sum of a Key Range in a BST
package p110

class TreeNode(val value: Int, var left: TreeNode? = null, var right: TreeNode? = null)

fun rangeSum(root: TreeNode?, lo: Int, hi: Int): Int {
    if (root == null) return 0
    if (root.value < lo) return rangeSum(root.right, lo, hi) // the left subtree is even smaller
    if (root.value > hi) return rangeSum(root.left, lo, hi)
    return root.value + rangeSum(root.left, lo, hi) + rangeSum(root.right, lo, hi)
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
    check(rangeSum(fromLevelOrder(listOf(20, 10, 30, 5, 15, 25, 40)), 12, 27) == 60)
    check(rangeSum(fromLevelOrder(listOf(8, 3, 12, null, 6)), 9, 11) == 0)
    check(rangeSum(fromLevelOrder(listOf()), 1, 100) == 0)
    check(rangeSum(fromLevelOrder(listOf(7)), 7, 7) == 7)
    check(rangeSum(fromLevelOrder(listOf(20, 10, 30, 5, 15, 25, 40)), 1, 100) == 145)
    check(rangeSum(fromLevelOrder(listOf(20, 10, 30, 5, 15, 25, 40)), 10, 20) == 45)
    println("110 ok")
}
