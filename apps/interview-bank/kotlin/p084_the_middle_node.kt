// 084 · The Middle Node
package p084

class ListNode(val value: Int, var next: ListNode? = null)

fun middleNode(head: ListNode?): ListNode? {
    var slow = head
    var fast = head
    while (fast?.next != null) {
        slow = slow?.next
        fast = fast.next?.next
    }
    return slow
}

fun fromMiddle(values: List<Int>): List<Int> = toList(middleNode(fromList(values)))

fun fromList(values: List<Int>): ListNode? {
    var head: ListNode? = null
    for (v in values.asReversed()) head = ListNode(v, head)
    return head
}

fun toList(head: ListNode?): List<Int> = generateSequence(head) { it.next }.map { it.value }.toList()

fun main() {
    check(fromMiddle(listOf(3, 1, 4, 1, 5)) == listOf(4, 1, 5))
    check(fromMiddle(listOf(2, 7, 1, 8)) == listOf(1, 8))
    check(fromMiddle(listOf(6)) == listOf(6))
    check(middleNode(null) == null)
    check(fromMiddle(listOf(9, 4)) == listOf(4))
    val head = fromList(listOf(10, 20, 30))
    check(middleNode(head) === head?.next)
    println("084 ok")
}
