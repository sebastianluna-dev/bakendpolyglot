// 120 · Serialize and Deserialize a Tree
package p120

class TreeNode(val value: Int, var left: TreeNode? = null, var right: TreeNode? = null)

fun serialize(root: TreeNode?): String {
    val tokens = mutableListOf<String>()
    fun write(node: TreeNode?) {
        if (node == null) {
            tokens.add("#")
            return
        }
        tokens.add(node.value.toString())
        write(node.left)
        write(node.right)
    }
    write(root)
    return tokens.joinToString(",")
}

fun deserialize(data: String): TreeNode? {
    val tokens = data.split(",").iterator()
    fun read(): TreeNode? {
        val token = tokens.next()
        if (token == "#") return null
        val node = TreeNode(token.toInt())
        node.left = read()
        node.right = read()
        return node
    }
    return read()
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
    check(serialize(fromLevelOrder(listOf(7, 3, 9, null, 4, 8))) == "7,3,#,4,#,#,9,8,#,#,#")
    check(serialize(fromLevelOrder(listOf())) == "#")
    check(toLevelOrder(deserialize("5,-2,#,#,#")) == listOf(5, -2))
    val trees = listOf(listOf(7, 3, 9, null, 4, 8), listOf(), listOf(-1000), listOf(1, null, 2, null, 3), listOf(10, -20, 30, -40, 50, -60, 70))
    for (tree in trees) {
        check(toLevelOrder(deserialize(serialize(fromLevelOrder(tree)))) == tree)
    }
    println("120 ok")
}
