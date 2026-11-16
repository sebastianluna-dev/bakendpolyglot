// 086 · Remove the k-th Node From the End
package p086

class ListNode(val value: Int, var next: ListNode? = null)

fun removeKthFromEnd(head: ListNode?, k: Int): ListNode? {
    val dummy = ListNode(0, head) // the node before the head, in case the head goes
    var lead = head
    for (i in 0 until k) lead = lead?.next
    var trail = dummy
    while (lead != null) {
        lead = lead.next
        trail = trail.next!!
    }
    trail.next = trail.next?.next
    return dummy.next
}

fun remove(values: List<Int>, k: Int): List<Int> = toList(removeKthFromEnd(fromList(values), k))

fun fromList(values: List<Int>): ListNode? {
    var head: ListNode? = null
    for (v in values.asReversed()) head = ListNode(v, head)
    return head
}

fun toList(head: ListNode?): List<Int> = generateSequence(head) { it.next }.map { it.value }.toList()

fun main() {
    check(remove(listOf(10, 20, 30, 40, 50), 2) == listOf(10, 20, 30, 50))
    check(remove(listOf(8), 1).isEmpty())
    check(remove(listOf(1, 2), 2) == listOf(2))
    check(remove(listOf(1, 2), 1) == listOf(1))
    check(remove(listOf(5, 6, 7), 3) == listOf(6, 7))
    check(remove(listOf(5, 6, 7), 1) == listOf(5, 6))
    println("086 ok")
}
