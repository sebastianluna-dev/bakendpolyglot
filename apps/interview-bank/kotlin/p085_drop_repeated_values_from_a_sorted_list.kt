// 085 · Drop Repeated Values From a Sorted List
package p085

class ListNode(val value: Int, var next: ListNode? = null)

fun dropRepeats(head: ListNode?): ListNode? {
    var node = head
    while (node != null) {
        val next = node.next ?: break
        if (next.value == node.value) node.next = next.next // stay: the new next may repeat too
        else node = next
    }
    return head
}

fun drop(values: List<Int>): List<Int> = toList(dropRepeats(fromList(values)))

fun fromList(values: List<Int>): ListNode? {
    var head: ListNode? = null
    for (v in values.asReversed()) head = ListNode(v, head)
    return head
}

fun toList(head: ListNode?): List<Int> = generateSequence(head) { it.next }.map { it.value }.toList()

fun main() {
    check(drop(listOf(1, 1, 2, 3, 3, 3)) == listOf(1, 2, 3))
    check(drop(listOf(4, 4, 4)) == listOf(4))
    check(drop(listOf()).isEmpty())
    check(drop(listOf(1, 2, 3)) == listOf(1, 2, 3))
    check(drop(listOf(-2, -2, 0, 5, 5)) == listOf(-2, 0, 5))
    check(drop(listOf(8)) == listOf(8))
    println("085 ok")
}
