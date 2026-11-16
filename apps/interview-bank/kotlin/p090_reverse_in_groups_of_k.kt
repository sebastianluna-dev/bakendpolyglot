// 090 · Reverse in Groups of k
package p090

class ListNode(val value: Int, var next: ListNode? = null)

fun reverseInGroups(head: ListNode?, k: Int): ListNode? {
    val dummy = ListNode(0, head)
    var before = dummy // the node just before the next group
    while (true) {
        var last: ListNode? = before
        for (i in 0 until k) last = last?.next
        if (last == null) return dummy.next // fewer than k nodes left: keep them as they are
        val after = last.next
        val first = before.next!!
        var prev = after
        var node: ListNode? = first
        while (node != null && node !== after) {
            val next = node.next
            node.next = prev
            prev = node
            node = next
        }
        before.next = last
        before = first
    }
}

fun groups(values: List<Int>, k: Int): List<Int> = toList(reverseInGroups(fromList(values), k))

fun fromList(values: List<Int>): ListNode? {
    var head: ListNode? = null
    for (v in values.asReversed()) head = ListNode(v, head)
    return head
}

fun toList(head: ListNode?): List<Int> = generateSequence(head) { it.next }.map { it.value }.toList()

fun main() {
    check(groups(listOf(1, 2, 3, 4, 5, 6, 7, 8), 3) == listOf(3, 2, 1, 6, 5, 4, 7, 8))
    check(groups(listOf(1, 2, 3, 4, 5, 6), 2) == listOf(2, 1, 4, 3, 6, 5))
    check(groups(listOf(4, 5, 6), 1) == listOf(4, 5, 6))
    check(groups(listOf(4, 5, 6), 3) == listOf(6, 5, 4))
    check(groups(listOf(4, 5, 6), 4) == listOf(4, 5, 6))
    check(groups(listOf(), 2).isEmpty())
    val head = fromList(listOf(1, 2, 3))
    check(reverseInGroups(head, 3)?.next?.next === head)
    println("090 ok")
}
