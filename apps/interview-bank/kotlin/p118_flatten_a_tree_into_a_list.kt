// 118 · Flatten a Tree Into a List
package p118

class TreeNode(val value: Int, var left: TreeNode? = null, var right: TreeNode? = null)

fun flatten(root: TreeNode?) {
    var node = root
    while (node != null) {
        val left = node.left
        if (left != null) {
            var tail: TreeNode = left // the last node of the left subtree in preorder
            while (tail.right != null) tail = tail.right!!
            tail.right = node.right
            node.right = left
            node.left = null
        }
        node = node.right
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

fun flattened(values: List<Int?>): List<Int?> {
    val root = fromLevelOrder(values)
    flatten(root)
    return toLevelOrder(root)
}

fun main() {
    check(flattened(listOf(10, 5, 20, 3, 7, null, 25)) == listOf(10, null, 5, null, 3, null, 7, null, 20, null, 25))
    check(flattened(listOf(1, 2, null, 3)) == listOf(1, null, 2, null, 3))
    check(flattened(listOf()).isEmpty())
    check(flattened(listOf(8)) == listOf(8))
    check(flattened(listOf(1, null, 2)) == listOf(1, null, 2))
    check(flattened(listOf(1, 2, 3, null, null, 4)) == listOf(1, null, 2, null, 3, null, 4))
    println("118 ok")
}
