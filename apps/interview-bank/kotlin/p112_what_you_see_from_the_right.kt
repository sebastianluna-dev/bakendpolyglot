// 112 · What You See From the Right
package p112

class TreeNode(val value: Int, var left: TreeNode? = null, var right: TreeNode? = null)

fun rightSideView(root: TreeNode?): List<Int> {
    val view = mutableListOf<Int>()
    fun visit(node: TreeNode?, depth: Int) {
        if (node == null) return
        if (depth == view.size) view.add(node.value) // first node reached at this depth
        visit(node.right, depth + 1)
        visit(node.left, depth + 1)
    }
    visit(root, 0)
    return view
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
    check(rightSideView(fromLevelOrder(listOf(10, 4, 15, 2, 6, null, 20, null, null, 5))) == listOf(10, 15, 20, 5))
    check(rightSideView(fromLevelOrder(listOf(1, 2))) == listOf(1, 2))
    check(rightSideView(fromLevelOrder(listOf())).isEmpty())
    check(rightSideView(fromLevelOrder(listOf(4))) == listOf(4))
    check(rightSideView(fromLevelOrder(listOf(1, 2, null, 3))) == listOf(1, 2, 3))
    check(rightSideView(fromLevelOrder(listOf(1, 2, 3, 4, 5, 6, 7))) == listOf(1, 3, 7))
    println("112 ok")
}
