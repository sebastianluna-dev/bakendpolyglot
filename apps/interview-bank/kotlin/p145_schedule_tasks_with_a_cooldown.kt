// 145 · Schedule Tasks With a Cooldown
package p145

import java.util.PriorityQueue

fun scheduleTime(tasks: String, cooldown: Int): Int {
    val counts = IntArray(26)
    for (t in tasks) counts[t - 'A']++
    val heap = PriorityQueue<Int>(compareByDescending { it }) // copies left per type, most first
    for (c in counts) if (c > 0) heap.add(c)
    val waiting = ArrayDeque<Pair<Int, Int>>() // (copies left, time it may run again)
    var time = 0
    while (heap.isNotEmpty() || waiting.isNotEmpty()) {
        time++
        if (heap.isEmpty()) {
            time = waiting.first().second // idle until the next type comes off cooldown
        } else {
            val left = heap.poll() - 1
            if (left > 0) waiting.addLast(left to time + cooldown)
        }
        if (waiting.isNotEmpty() && waiting.first().second == time) heap.add(waiting.removeFirst().first)
    }
    return time
}

fun main() {
    check(scheduleTime("AAABBC", 2) == 7)
    check(scheduleTime("AABBCD", 1) == 6)
    check(scheduleTime("AAABBB", 2) == 8)
    check(scheduleTime("AAAA", 3) == 13)
    check(scheduleTime("ABAB", 0) == 4)
    check(scheduleTime("A", 5) == 1)
    check(scheduleTime("AAABCDEFG", 2) == 9)
    println("145 ok")
}
