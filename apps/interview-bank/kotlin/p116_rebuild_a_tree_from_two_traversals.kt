// 116 · Rebuild a Tree From Two Traversals
package p116

class TreeNode(val value: Int, var left: TreeNode? = null, var right: TreeNode? = null)

fun buildTree(preorder: IntArray, inorder: IntArray): TreeNode? {
    val position = HashMap<Int, Int>()
    inorder.forEachIndexed { i, value -> position[value] = i }

    // builds the subtree whose preorder starts at pre and whose inorder is inorder[lo..hi]
    fun build(pre: Int, lo: Int, hi: Int): TreeNode? {
        if (lo > hi) return null
        val root = TreeNode(preorder[pre])
        val mid = position.getValue(root.value)
        root.left = build(pre + 1, lo, mid - 1)
        root.right = build(pre + 1 + (mid - lo), mid + 1, hi)
        return root
    }
    return build(0, 0, inorder.size - 1)
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
    check(toLevelOrder(buildTree(intArrayOf(8, 4, 2, 6, 12, 14), intArrayOf(2, 4, 6, 8, 12, 14))) == listOf(8, 4, 12, 2, 6, null, 14))
    check(toLevelOrder(buildTree(intArrayOf(1, 2, 3), intArrayOf(3, 2, 1))) == listOf(1, 2, null, 3))
    check(toLevelOrder(buildTree(intArrayOf(), intArrayOf())).isEmpty())
    check(toLevelOrder(buildTree(intArrayOf(5), intArrayOf(5))) == listOf(5))
    check(toLevelOrder(buildTree(intArrayOf(1, 2, 3), intArrayOf(1, 2, 3))) == listOf(1, null, 2, null, 3))
    check(toLevelOrder(buildTree(intArrayOf(-1, -3, 0), intArrayOf(-3, -1, 0))) == listOf(-1, -3, 0))
    println("116 ok")
}
