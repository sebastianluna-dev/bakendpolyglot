// 100 · Config Values at a Point in Time
package p100

class ConfigHistory {
    private val history = HashMap<String, MutableList<Pair<Int, String>>>() // (timestamp, value), oldest first

    fun set(key: String, value: String, timestamp: Int) {
        // timestamps only grow, so the list stays sorted
        history.getOrPut(key) { mutableListOf() }.add(timestamp to value)
    }

    fun get(key: String, timestamp: Int): String {
        val entries = history[key] ?: return ""
        var lo = 0
        var hi = entries.size // looking for the first entry set after timestamp
        while (lo < hi) {
            val mid = lo + (hi - lo) / 2
            if (entries[mid].first > timestamp) hi = mid else lo = mid + 1
        }
        return if (lo == 0) "" else entries[lo - 1].second
    }
}

fun main() {
    val h = ConfigHistory()
    check(h.get("hold.minutes", 100) == "")
    h.set("hold.minutes", "10", 100)
    h.set("hold.minutes", "15", 250)
    h.set("currency", "EUR", 120)
    check(h.get("hold.minutes", 99) == "")
    check(h.get("hold.minutes", 100) == "10")
    check(h.get("hold.minutes", 249) == "10")
    check(h.get("hold.minutes", 250) == "15")
    check(h.get("hold.minutes", 1000) == "15")
    check(h.get("currency", 119) == "")
    check(h.get("currency", 500) == "EUR")
    check(h.get("refunds", 500) == "")
    h.set("hold.minutes", "20", 400)
    check(h.get("hold.minutes", 399) == "15")
    check(h.get("hold.minutes", 400) == "20")
    println("100 ok")
}
