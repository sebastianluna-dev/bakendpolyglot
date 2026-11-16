// 082 · Merge Two Sorted Lists
package p082

class ListNode(val value: Int, var next: ListNode? = null)

fun mergeSorted(a: ListNode?, b: ListNode?): ListNode? {
    val dummy = ListNode(0)
    var tail = dummy
    var x = a
    var y = b
    while (x != null && y != null) {
        if (x.value <= y.value) {
            tail.next = x
            tail = x
            x = x.next
        } else {
            tail.next = y
            tail = y
            y = y.next
        }
    }
    tail.next = x ?: y // whatever is left is already sorted
    return dummy.next
}

fun merge(a: List<Int>, b: List<Int>): List<Int> = toList(mergeSorted(fromList(a), fromList(b)))

fun fromList(values: List<Int>): ListNode? {
    var head: ListNode? = null
    for (v in values.asReversed()) head = ListNode(v, head)
    return head
}

fun toList(head: ListNode?): List<Int> = generateSequence(head) { it.next }.map { it.value }.toList()

fun main() {
    check(merge(listOf(1, 4, 6), listOf(2, 3, 6, 9)) == listOf(1, 2, 3, 4, 6, 6, 9))
    check(merge(listOf(), listOf(5, 8)) == listOf(5, 8))
    check(merge(listOf(), listOf()).isEmpty())
    check(merge(listOf(7, 8), listOf(1, 2)) == listOf(1, 2, 7, 8))
    check(merge(listOf(-3, 0), listOf(-3)) == listOf(-3, -3, 0))
    val first = fromList(listOf(2))
    val second = fromList(listOf(2))
    val merged = mergeSorted(first, second)
    check(merged === first && merged?.next === second)
    println("082 ok")
}
