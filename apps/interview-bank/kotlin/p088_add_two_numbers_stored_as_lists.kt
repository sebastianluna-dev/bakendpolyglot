// 088 · Add Two Numbers Stored as Lists
package p088

class ListNode(val value: Int, var next: ListNode? = null)

fun addLists(a: ListNode?, b: ListNode?): ListNode? {
    val dummy = ListNode(0)
    var tail = dummy
    var x = a
    var y = b
    var carry = 0
    while (x != null || y != null || carry > 0) {
        val sum = carry + (x?.value ?: 0) + (y?.value ?: 0)
        carry = sum / 10
        val digit = ListNode(sum % 10)
        tail.next = digit
        tail = digit
        x = x?.next
        y = y?.next
    }
    return dummy.next
}

fun add(a: List<Int>, b: List<Int>): List<Int> = toList(addLists(fromList(a), fromList(b)))

fun fromList(values: List<Int>): ListNode? {
    var head: ListNode? = null
    for (v in values.asReversed()) head = ListNode(v, head)
    return head
}

fun toList(head: ListNode?): List<Int> = generateSequence(head) { it.next }.map { it.value }.toList()

fun main() {
    check(add(listOf(5, 2, 9), listOf(8, 4)) == listOf(3, 7, 9))
    check(add(listOf(9, 9, 9), listOf(1)) == listOf(0, 0, 0, 1))
    check(add(listOf(0), listOf(0)) == listOf(0))
    check(add(listOf(1), listOf(6, 0, 3)) == listOf(7, 0, 3))
    check(add(listOf(9, 9), listOf(9, 9)) == listOf(8, 9, 1))
    check(add(List(20) { 9 }, listOf(1)) == List(20) { 0 } + 1)
    println("088 ok")
}
