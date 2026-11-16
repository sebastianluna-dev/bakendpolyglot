// 106 · Is the Tree Symmetric?
package p106

class TreeNode(val value: Int, var left: TreeNode? = null, var right: TreeNode? = null)

fun isSymmetric(root: TreeNode?): Boolean = root == null || isMirror(root.left, root.right)

fun isMirror(a: TreeNode?, b: TreeNode?): Boolean {
    if (a == null || b == null) return a == b
    return a.value == b.value && isMirror(a.left, b.right) && isMirror(a.right, b.left)
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
    check(isSymmetric(fromLevelOrder(listOf(7, 4, 4, 1, 9, 9, 1))))
    check(!isSymmetric(fromLevelOrder(listOf(5, 3, 3, 8, null, null, 2))))
    check(isSymmetric(fromLevelOrder(listOf())))
    check(isSymmetric(fromLevelOrder(listOf(1))))
    check(!isSymmetric(fromLevelOrder(listOf(2, 3, 3, null, 4, null, 4))))
    check(!isSymmetric(fromLevelOrder(listOf(1, 2, 3))))
    println("106 ok")
}
