// 081 · Reverse a Linked List
package p081

class ListNode(val value: Int, var next: ListNode? = null)

fun reverseList(head: ListNode?): ListNode? {
    var prev: ListNode? = null
    var node = head
    while (node != null) {
        val next = node.next // save it before the link is overwritten
        node.next = prev
        prev = node
        node = next
    }
    return prev
}

fun fromList(values: List<Int>): ListNode? {
    var head: ListNode? = null
    for (v in values.asReversed()) head = ListNode(v, head)
    return head
}

fun toList(head: ListNode?): List<Int> = generateSequence(head) { it.next }.map { it.value }.toList()

fun main() {
    check(toList(reverseList(fromList(listOf(4, 8, 15, 16, 23)))) == listOf(23, 16, 15, 8, 4))
    check(toList(reverseList(fromList(listOf(2, 9)))) == listOf(9, 2))
    check(toList(reverseList(fromList(listOf(7)))) == listOf(7))
    check(reverseList(fromList(listOf())) == null)
    check(toList(reverseList(fromList(listOf(5, 5, 1)))) == listOf(1, 5, 5))
    println("081 ok")
}
