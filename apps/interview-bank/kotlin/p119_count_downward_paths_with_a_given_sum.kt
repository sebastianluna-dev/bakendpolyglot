// 119 · Count Downward Paths With a Given Sum
package p119

class TreeNode(val value: Int, var left: TreeNode? = null, var right: TreeNode? = null)

fun countPathsWithSum(root: TreeNode?, target: Int): Int {
    val seen = hashMapOf(0L to 1) // prefix sums on the current root path -> count
    fun visit(node: TreeNode?, prefix: Long): Int {
        if (node == null) return 0
        val total = prefix + node.value
        var count = seen.getOrDefault(total - target, 0)
        seen[total] = seen.getOrDefault(total, 0) + 1
        count += visit(node.left, total) + visit(node.right, total)
        seen[total] = seen.getValue(total) - 1
        return count
    }
    return visit(root, 0L)
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
    check(countPathsWithSum(fromLevelOrder(listOf(4, 2, -1, 1, 3, null, 5)), 5) == 2)
    check(countPathsWithSum(fromLevelOrder(listOf(1, -1, 1, 1)), 1) == 4)
    check(countPathsWithSum(fromLevelOrder(listOf()), 0) == 0)
    check(countPathsWithSum(fromLevelOrder(listOf(0, 0, null, 0)), 0) == 6)
    check(countPathsWithSum(fromLevelOrder(listOf(1000000000, null, 1000000000)), 2000000000) == 1)
    check(countPathsWithSum(fromLevelOrder(listOf(1000000000, null, 1000000000)), -294967296) == 0)
    println("119 ok")
}
