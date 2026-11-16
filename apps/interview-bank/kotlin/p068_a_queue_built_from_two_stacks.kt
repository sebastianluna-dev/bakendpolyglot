// 068 · A Queue Built From Two Stacks
package p068

class TwoStackQueue {
    private val inbox = ArrayDeque<Int>() // newest at the end
    private val outbox = ArrayDeque<Int>() // oldest at the end

    fun enqueue(x: Int) {
        inbox.addLast(x)
    }

    fun dequeue(): Int {
        refill()
        return outbox.removeLast()
    }

    fun peek(): Int {
        refill()
        return outbox.last()
    }

    fun isEmpty(): Boolean = inbox.isEmpty() && outbox.isEmpty()

    // Only when outbox is empty, or newer items would jump ahead of older ones.
    private fun refill() {
        if (outbox.isNotEmpty()) return
        while (inbox.isNotEmpty()) outbox.addLast(inbox.removeLast())
    }
}

fun main() {
    val q = TwoStackQueue()
    check(q.isEmpty())
    q.enqueue(1)
    q.enqueue(2)
    check(q.peek() == 1)
    check(q.dequeue() == 1)
    q.enqueue(3)
    check(q.dequeue() == 2)
    check(q.peek() == 3)
    check(!q.isEmpty())
    check(q.dequeue() == 3)
    check(q.isEmpty())
    q.enqueue(4)
    q.enqueue(5)
    check(q.dequeue() == 4)
    q.enqueue(6)
    check(q.dequeue() == 5)
    check(q.dequeue() == 6)
    check(q.isEmpty())
    println("068 ok")
}
