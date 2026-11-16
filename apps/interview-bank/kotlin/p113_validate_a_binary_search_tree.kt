// 113 · Validate a Binary Search Tree
package p113

class TreeNode(val value: Int, var left: TreeNode? = null, var right: TreeNode? = null)

fun isSearchTree(root: TreeNode?): Boolean = within(root, null, null)

// lo and hi are the nearest ancestors the key must stay above and below; null means no bound
fun within(node: TreeNode?, lo: TreeNode?, hi: TreeNode?): Boolean {
    if (node == null) return true
    if (lo != null && node.value <= lo.value) return false
    if (hi != null && node.value >= hi.value) return false
    return within(node.left, lo, node) && within(node.right, node, hi)
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
    check(isSearchTree(fromLevelOrder(listOf(8, 4, 12, 2, 6, 10, 14))))
    check(!isSearchTree(fromLevelOrder(listOf(8, 4, 12, 2, 9, 10, 14))))
    check(isSearchTree(fromLevelOrder(listOf())))
    check(!isSearchTree(fromLevelOrder(listOf(5, 5))))
    check(isSearchTree(fromLevelOrder(listOf(Int.MIN_VALUE, null, Int.MAX_VALUE))))
    check(!isSearchTree(fromLevelOrder(listOf(Int.MAX_VALUE, Int.MAX_VALUE))))
    check(!isSearchTree(fromLevelOrder(listOf(Int.MIN_VALUE, Int.MIN_VALUE))))
    println("113 ok")
}
