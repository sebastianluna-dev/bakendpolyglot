// 200 · Consistent Hash Ring
package p200

fun fnv1a(s: String): Int {
    var h = 0x811c9dc5.toInt() // the literal is a Long in Kotlin, so convert it
    for (b in s.encodeToByteArray()) {
        h = h xor (b.toInt() and 0xff) // Byte is signed: without the mask, bytes over 127 flip the high bits
        h *= 0x01000193 // Int arithmetic wraps at 2^32, as the hash needs
    }
    return h
}

fun mix32(x: Int): Int {
    var h = x
    h = h xor (h ushr 16)
    h *= 0x85ebca6b.toInt()
    h = h xor (h ushr 13)
    h *= 0xc2b2ae35.toInt()
    h = h xor (h ushr 16)
    return h
}

fun ringHash(s: String): Long = mix32(fnv1a(s)).toLong() and 0xffffffffL // compare as unsigned

class HashRing(private val replicas: Int) {
    private val nodes = HashSet<String>()
    private val points = mutableListOf<Pair<Long, String>>() // (hash, node), sorted by hash, then node

    fun addNode(node: String) {
        if (!nodes.add(node)) return
        for (i in 0 until replicas) points.add(ringHash("$node#$i") to node)
        points.sortWith(compareBy({ it.first }, { it.second }))
    }

    fun removeNode(node: String) {
        if (!nodes.remove(node)) return
        points.removeAll { it.second == node }
    }

    fun nodeFor(key: String): String {
        if (points.isEmpty()) return ""
        val h = ringHash(key)
        var lo = 0
        var hi = points.size // looking for the first point with hash >= h
        while (lo < hi) {
            val mid = lo + (hi - lo) / 2
            if (points[mid].first >= h) hi = mid else lo = mid + 1
        }
        return points[lo % points.size].second // past the last point: wrap to the first
    }
}

val KEYS = listOf("book:1", "book:2", "book:3", "book:4", "book:5", "book:6", "book:7", "book:37")

fun owners(ring: HashRing): String = KEYS.joinToString("") { ring.nodeFor(it).takeLast(1) } // the last letter of each key's node

fun main() {
    check(fnv1a("") == 0x811c9dc5.toInt())
    check(fnv1a("a") == 0xe40c292c.toInt())
    check(fnv1a("foobar") == 0xbf9cf968.toInt())
    check(mix32(0) == 0)
    check(ringHash("cache-a#0") == 0x6901e554L)
    check(ringHash("book:37") == 0xfcfa1e45L)

    val ring = HashRing(3)
    check(ring.nodeFor("book:1") == "")
    for (name in listOf("cache-a", "cache-b", "cache-c")) ring.addNode(name)
    check(owners(ring) == "babccccc")
    ring.addNode("cache-a")
    ring.removeNode("cache-z")
    check(owners(ring) == "babccccc")
    ring.removeNode("cache-b")
    check(owners(ring) == "aacccccc") // only cache-b's keys moved
    ring.addNode("cache-b")
    check(owners(ring) == "babccccc")
    ring.addNode("cache-d")
    check(owners(ring) == "babdccdc") // keys move only to the new node
    for (name in listOf("cache-a", "cache-b", "cache-c", "cache-d")) ring.removeNode(name)
    check(ring.nodeFor("book:1") == "")
    println("200 ok")
}
