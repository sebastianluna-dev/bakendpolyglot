// 104 · Mirror a Tree
package p104

class TreeNode(val value: Int, var left: TreeNode? = null, var right: TreeNode? = null)

fun mirrorTree(root: TreeNode?): TreeNode? {
    if (root == null) return null
    val left = root.left // keep it: the next line overwrites root.left
    root.left = mirrorTree(root.right)
    root.right = mirrorTree(left)
    return root
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

fun toLevelOrder(root: TreeNode?): List<Int?> {
    if (root == null) return listOf()
    val values = mutableListOf<Int?>(root.value)
    val queue = ArrayDeque(listOf(root))
    while (queue.isNotEmpty()) {
        val node = queue.removeFirst()
        for (child in listOf(node.left, node.right)) {
            values.add(child?.value)
            if (child != null) queue.addLast(child)
        }
    }
    while (values.last() == null) values.removeAt(values.lastIndex)
    return values
}

fun main() {
    check(toLevelOrder(mirrorTree(fromLevelOrder(listOf(6, 2, 8, 1, 4, null, 9)))) == listOf(6, 8, 2, 9, null, 4, 1))
    check(toLevelOrder(mirrorTree(fromLevelOrder(listOf(3, 1)))) == listOf(3, null, 1))
    check(toLevelOrder(mirrorTree(fromLevelOrder(listOf()))).isEmpty())
    check(toLevelOrder(mirrorTree(fromLevelOrder(listOf(7)))) == listOf(7))
    check(toLevelOrder(mirrorTree(fromLevelOrder(listOf(1, 2, null, 3)))) == listOf(1, null, 2, null, 3))
    println("104 ok")
}
