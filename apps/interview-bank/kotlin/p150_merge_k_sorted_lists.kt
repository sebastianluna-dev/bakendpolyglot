// 150 · Merge k Sorted Lists
package p150

import java.util.PriorityQueue

class ListNode(val value: Int, var next: ListNode? = null)

fun mergeLists(lists: List<ListNode?>): ListNode? {
    val heap = PriorityQueue<ListNode>(compareBy { it.value }) // the front node of each list
    for (head in lists) if (head != null) heap.add(head)
    val dummy = ListNode(0)
    var tail = dummy
    while (heap.isNotEmpty()) {
        val node = heap.poll()
        tail.next = node
        tail = node
        node.next?.let { heap.add(it) }
    }
    return dummy.next
}

fun merge(lists: List<List<Int>>): List<Int> = toList(mergeLists(lists.map(::fromList)))

fun fromList(values: List<Int>): ListNode? {
    var head: ListNode? = null
    for (v in values.asReversed()) head = ListNode(v, head)
    return head
}

fun toList(head: ListNode?): List<Int> = generateSequence(head) { it.next }.map { it.value }.toList()

fun main() {
    check(merge(listOf(listOf(1, 4, 7), listOf(2, 5), listOf(0, 6, 9))) == listOf(0, 1, 2, 4, 5, 6, 7, 9))
    check(merge(listOf(listOf(), listOf(3), listOf())) == listOf(3))
    check(merge(listOf()).isEmpty())
    check(merge(listOf(listOf())).isEmpty())
    check(merge(listOf(listOf(2, 2), listOf(2), listOf(1, 2))) == listOf(1, 2, 2, 2, 2))
    check(merge(listOf(listOf(-5, 0, 5))) == listOf(-5, 0, 5))
    check(merge(listOf(listOf(10), listOf(-10), listOf(0), listOf(-20, 20))) == listOf(-20, -10, 0, 10, 20))
    println("150 ok")
}
