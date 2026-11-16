// 188 · Rooms Needed for All Meetings
package p188

fun roomsNeeded(meetings: Array<IntArray>): Int {
    val starts = meetings.map { it[0] }.sorted()
    val ends = meetings.map { it[1] }.sorted()
    var rooms = 0
    var j = 0 // ends[j] is the earliest end that has not freed a room yet
    for (start in starts) {
        if (start >= ends[j]) {
            j++ // a meeting is over: reuse its room
        } else {
            rooms++
        }
    }
    return rooms
}

fun ivs(vararg pairs: Pair<Int, Int>): Array<IntArray> = pairs.map { intArrayOf(it.first, it.second) }.toTypedArray()

fun main() {
    check(roomsNeeded(ivs(9 to 12, 10 to 11, 11 to 13, 13 to 14)) == 2)
    check(roomsNeeded(ivs(8 to 10, 10 to 12, 12 to 14)) == 1)
    check(roomsNeeded(ivs()) == 0)
    check(roomsNeeded(ivs(1 to 5, 2 to 6, 3 to 7)) == 3)
    check(roomsNeeded(ivs(2 to 3, 2 to 3)) == 2)
    check(roomsNeeded(ivs(5 to 10, 1 to 4, 4 to 6)) == 2)
    println("188 ok")
}
