// 195 · LRU Cache
package p195

class Node(val key: Int, var value: Int) {
    var prev: Node = this
    var next: Node = this
}

class LruCache(private val capacity: Int) {
    private val nodes = HashMap<Int, Node>()
    private val head = Node(0, 0) // sentinel: head.next is the most recent, head.prev the least recent

    fun get(key: Int): Int {
        val node = nodes[key] ?: return -1
        unlink(node)
        pushFront(node)
        return node.value
    }

    fun put(key: Int, value: Int) {
        val existing = nodes[key]
        if (existing != null) {
            existing.value = value
            unlink(existing)
            pushFront(existing)
            return
        }
        if (nodes.size == capacity) {
            val oldest = head.prev
            unlink(oldest)
            nodes.remove(oldest.key)
        }
        val node = Node(key, value)
        nodes[key] = node
        pushFront(node)
    }

    private fun unlink(node: Node) {
        node.prev.next = node.next
        node.next.prev = node.prev
    }

    private fun pushFront(node: Node) {
        node.prev = head
        node.next = head.next
        head.next.prev = node
        head.next = node
    }
}

fun main() {
    val c = LruCache(2)
    c.put(1, 10)
    c.put(2, 20)
    check(c.get(1) == 10)
    c.put(3, 30) // evicts 2
    check(c.get(2) == -1)
    check(c.get(3) == 30)
    c.put(1, 11) // an update counts as a use
    c.put(4, 40) // evicts 3
    check(c.get(3) == -1)
    check(c.get(1) == 11)
    check(c.get(4) == 40)

    val one = LruCache(1)
    one.put(5, 50)
    check(one.get(5) == 50)
    one.put(6, 60)
    check(one.get(5) == -1)
    check(one.get(6) == 60)
    check(one.get(7) == -1)
    one.put(6, 61)
    check(one.get(6) == 61)
    println("195 ok")
}
