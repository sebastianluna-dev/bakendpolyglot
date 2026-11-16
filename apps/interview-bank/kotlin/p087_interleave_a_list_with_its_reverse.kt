// 087 · Interleave a List With Its Reverse
package p087

class ListNode(val value: Int, var next: ListNode? = null)

fun interleaveWithReverse(head: ListNode?) {
    var slow = head
    var fast = head
    while (fast?.next?.next != null) {
        slow = slow?.next
        fast = fast.next?.next
    }
    var second = reverse(slow?.next)
    slow?.next = null // cut the first half off
    var first = head
    while (first != null && second != null) {
        val nextFirst = first.next
        val nextSecond = second.next
        first.next = second
        second.next = nextFirst
        first = nextFirst
        second = nextSecond
    }
}

fun reverse(head: ListNode?): ListNode? {
    var prev: ListNode? = null
    var node = head
    while (node != null) {
        val next = node.next
        node.next = prev
        prev = node
        node = next
    }
    return prev
}

fun interleaved(values: List<Int>): List<Int> {
    val head = fromList(values)
    interleaveWithReverse(head)
    return toList(head)
}

fun fromList(values: List<Int>): ListNode? {
    var head: ListNode? = null
    for (v in values.asReversed()) head = ListNode(v, head)
    return head
}

fun toList(head: ListNode?): List<Int> = generateSequence(head) { it.next }.map { it.value }.toList()

fun main() {
    check(interleaved(listOf(1, 2, 3, 4, 5, 6)) == listOf(1, 6, 2, 5, 3, 4))
    check(interleaved(listOf(3, 8, 5, 2, 9)) == listOf(3, 9, 8, 2, 5))
    check(interleaved(listOf()).isEmpty())
    check(interleaved(listOf(7)) == listOf(7))
    check(interleaved(listOf(1, 2)) == listOf(1, 2))
    check(interleaved(listOf(10, 20, 30)) == listOf(10, 30, 20))
    check(interleaved(listOf(1, 2, 3, 4)) == listOf(1, 4, 2, 3))
    println("087 ok")
}
