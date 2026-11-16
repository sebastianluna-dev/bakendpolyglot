// 111 · Level-by-Level Traversal
package p111

class TreeNode(val value: Int, var left: TreeNode? = null, var right: TreeNode? = null)

fun levelOrder(root: TreeNode?): List<List<Int>> {
    val levels = mutableListOf<List<Int>>()
    var level = listOfNotNull(root)
    while (level.isNotEmpty()) {
        levels.add(level.map { it.value })
        level = level.flatMap { listOfNotNull(it.left, it.right) }
    }
    return levels
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
    check(levelOrder(fromLevelOrder(listOf(8, 4, 12, 2, null, 10, 14))) == listOf(listOf(8), listOf(4, 12), listOf(2, 10, 14)))
    check(levelOrder(fromLevelOrder(listOf(5, 6, null, 7, 8))) == listOf(listOf(5), listOf(6), listOf(7, 8)))
    check(levelOrder(fromLevelOrder(listOf())).isEmpty())
    check(levelOrder(fromLevelOrder(listOf(9))) == listOf(listOf(9)))
    check(levelOrder(fromLevelOrder(listOf(1, 2, 3, null, 4, 5, null, null, null, 6))) == listOf(listOf(1), listOf(2, 3), listOf(4, 5), listOf(6)))
    println("111 ok")
}
