// 083 · Does the List Loop?
package p083

class ListNode(val value: Int, var next: ListNode? = null)

fun hasLoop(head: ListNode?): Boolean {
    var slow = head
    var fast = head
    while (fast?.next != null) {
        slow = slow?.next
        fast = fast.next?.next
        if (slow === fast) return true
    }
    return false
}

// Builds the list and links its last node to the node at index loopTo (-1: no loop).
fun withLoop(values: List<Int>, loopTo: Int): ListNode? {
    val nodes = values.map { ListNode(it) }
    for (i in 0 until nodes.size - 1) nodes[i].next = nodes[i + 1]
    if (loopTo >= 0) nodes.last().next = nodes[loopTo]
    return nodes.firstOrNull()
}

fun main() {
    check(hasLoop(withLoop(listOf(3, 8, 1, 6), 1)))
    check(!hasLoop(withLoop(listOf(4, 2), -1)))
    check(hasLoop(withLoop(listOf(5), 0)))
    check(!hasLoop(withLoop(listOf(), -1)))
    check(!hasLoop(withLoop(listOf(2, 5, 2), -1)))
    check(hasLoop(withLoop(listOf(1, 2, 3, 4, 5, 6), 0)))
    check(hasLoop(withLoop(listOf(7, 1), 1)))
    println("083 ok")
}
