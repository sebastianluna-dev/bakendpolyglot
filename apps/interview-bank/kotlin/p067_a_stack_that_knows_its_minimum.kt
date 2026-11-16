// 067 · A Stack That Knows Its Minimum
package p067

class MinStack {
    private val values = ArrayDeque<Int>()
    private val mins = ArrayDeque<Int>() // mins[i] is the smallest of values[0..i]

    fun push(x: Int) {
        values.addLast(x)
        mins.addLast(if (mins.isEmpty()) x else minOf(x, mins.last()))
    }

    fun pop(): Int {
        mins.removeLast()
        return values.removeLast()
    }

    fun top(): Int = values.last()

    fun min(): Int = mins.last()
}

fun main() {
    val s = MinStack()
    s.push(5)
    s.push(3)
    s.push(7)
    check(s.min() == 3)
    check(s.top() == 7)
    check(s.pop() == 7)
    check(s.min() == 3)
    check(s.pop() == 3)
    check(s.min() == 5)
    check(s.pop() == 5)

    s.push(4)
    s.push(1)
    s.push(1)
    check(s.pop() == 1)
    check(s.min() == 1)
    check(s.pop() == 1)
    check(s.min() == 4)
    s.push(-2)
    check(s.min() == -2)
    check(s.top() == -2)
    check(s.pop() == -2)
    check(s.top() == 4)
    check(s.min() == 4)
    println("067 ok")
}
