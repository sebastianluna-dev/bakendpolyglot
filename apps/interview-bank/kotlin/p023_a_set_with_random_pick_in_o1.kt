// 023 · A Set With Random Pick in O(1)
package p023

class RandomSet(private val randomIndex: (Int) -> Int) {
    private val items = mutableListOf<Int>()
    private val position = HashMap<Int, Int>() // value -> its index in items

    fun add(value: Int): Boolean {
        if (value in position) return false
        position[value] = items.size
        items.add(value)
        return true
    }

    fun remove(value: Int): Boolean {
        val i = position[value] ?: return false
        val last = items.removeAt(items.size - 1)
        if (last != value) {
            items[i] = last
            position[last] = i
        }
        position.remove(value)
        return true
    }

    fun pick(): Int = items[randomIndex(items.size)]
}

fun picks(set: RandomSet, count: Int): List<Int> = List(count) { set.pick() }.sorted()

fun main() {
    var calls = 0
    val set = RandomSet { n -> calls++ % n }
    check(set.add(12))
    check(set.add(7))
    check(!set.add(12))
    check(set.add(30))
    check(set.remove(7))
    check(!set.remove(7))
    check(!set.remove(99))
    check(picks(set, 2) == listOf(12, 30))
    check(set.add(5))
    check(picks(set, 3) == listOf(5, 12, 30))
    check(set.remove(30))
    check(set.remove(5))
    check(set.add(5))
    check(set.remove(12))
    check(set.pick() == 5)
    println("023 ok")
}
