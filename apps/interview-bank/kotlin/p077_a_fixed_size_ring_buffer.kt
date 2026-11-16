// 077 · A Fixed-Size Ring Buffer
package p077

class RingBuffer(capacity: Int) {
    private val items = IntArray(capacity)
    private var head = 0 // index of the oldest value
    private var count = 0

    fun enqueue(value: Int): Boolean {
        if (isFull()) return false
        items[(head + count) % items.size] = value
        count++
        return true
    }

    fun dequeue(): Int {
        if (isEmpty()) return -1
        val value = items[head]
        head = (head + 1) % items.size
        count--
        return value
    }

    fun front(): Int = if (isEmpty()) -1 else items[head]

    fun rear(): Int = if (isEmpty()) -1 else items[(head + count - 1) % items.size]

    fun isEmpty(): Boolean = count == 0

    fun isFull(): Boolean = count == items.size
}

fun main() {
    val b = RingBuffer(3)
    check(b.isEmpty())
    check(b.enqueue(5))
    check(b.enqueue(8))
    check(b.enqueue(2))
    check(!b.enqueue(9))
    check(b.isFull())
    check(b.rear() == 2)
    check(b.dequeue() == 5)
    check(b.enqueue(9))
    check(b.front() == 8)
    check(b.rear() == 9)
    check(b.dequeue() == 8)
    check(b.dequeue() == 2)
    check(b.dequeue() == 9)
    check(b.dequeue() == -1)
    check(b.isEmpty())
    check(b.front() == -1)
    check(b.rear() == -1)

    val one = RingBuffer(1)
    check(one.enqueue(0))
    check(one.isFull())
    check(!one.enqueue(1))
    check(one.front() == 0)
    check(one.rear() == 0)
    check(one.dequeue() == 0)
    check(one.isEmpty())
    println("077 ok")
}
