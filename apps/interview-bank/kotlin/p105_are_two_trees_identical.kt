// 105 · Are Two Trees Identical?
package p105

class TreeNode(val value: Int, var left: TreeNode? = null, var right: TreeNode? = null)

fun areIdentical(a: TreeNode?, b: TreeNode?): Boolean {
    if (a == null || b == null) return a == b
    return a.value == b.value && areIdentical(a.left, b.left) && areIdentical(a.right, b.right)
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
    check(areIdentical(fromLevelOrder(listOf(4, 2, 6)), fromLevelOrder(listOf(4, 2, 6))))
    check(!areIdentical(fromLevelOrder(listOf(4, 2)), fromLevelOrder(listOf(4, null, 2))))
    check(areIdentical(fromLevelOrder(listOf()), fromLevelOrder(listOf())))
    check(!areIdentical(fromLevelOrder(listOf(1)), fromLevelOrder(listOf())))
    check(!areIdentical(fromLevelOrder(listOf(1, 2, 3)), fromLevelOrder(listOf(1, 2, 4))))
    check(areIdentical(fromLevelOrder(listOf(5, null, 8, 7)), fromLevelOrder(listOf(5, null, 8, 7))))
    println("105 ok")
}
