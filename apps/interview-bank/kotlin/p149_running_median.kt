// 149 · Running Median
package p149

import java.util.PriorityQueue

class RunningMedian {
    private val low = PriorityQueue<Int>(compareByDescending { it }) // the smaller half, largest on top
    private val high = PriorityQueue<Int>() // the larger half, smallest on top

    fun addNum(num: Int) {
        low.add(num)
        high.add(low.poll())
        if (high.size > low.size) low.add(high.poll())
    }

    fun findMedian(): Double {
        if (low.size > high.size) return low.peek().toDouble()
        return (low.peek().toDouble() + high.peek()) / 2 // double first: the int sum can overflow
    }
}

fun main() {
    val m = RunningMedian()
    m.addNum(5)
    check(m.findMedian() == 5.0)
    m.addNum(15)
    check(m.findMedian() == 10.0)
    m.addNum(1)
    check(m.findMedian() == 5.0)
    m.addNum(3)
    check(m.findMedian() == 4.0)

    val neg = RunningMedian()
    neg.addNum(-2)
    check(neg.findMedian() == -2.0)
    neg.addNum(-2)
    check(neg.findMedian() == -2.0)
    neg.addNum(7)
    check(neg.findMedian() == -2.0)
    neg.addNum(8)
    check(neg.findMedian() == 2.5)

    val down = RunningMedian()
    val medians = mutableListOf<Double>()
    for (x in intArrayOf(9, 8, 7, 6, 5)) {
        down.addNum(x)
        medians.add(down.findMedian())
    }
    check(medians == listOf(9.0, 8.5, 8.0, 7.5, 7.0))

    val big = RunningMedian()
    big.addNum(Int.MAX_VALUE)
    big.addNum(Int.MAX_VALUE)
    check(big.findMedian() == Int.MAX_VALUE.toDouble())
    big.addNum(Int.MIN_VALUE)
    big.addNum(Int.MIN_VALUE)
    check(big.findMedian() == -0.5)
    println("149 ok")
}
