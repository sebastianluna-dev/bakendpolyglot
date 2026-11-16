// 192 · Keep the Latest Version of Each Record
package p192

data class Revision(val id: Int, val version: Int, val payload: String)

fun latestVersions(revisions: List<Revision>): List<Revision> {
    val latest = HashMap<Int, Revision>()
    for (r in revisions) {
        val kept = latest[r.id]
        if (kept == null || r.version > kept.version) latest[r.id] = r // strictly greater: the first of equal versions stays
    }
    return latest.values.sortedBy { it.id }
}

fun main() {
    val r = ::Revision
    check(
        latestVersions(listOf(r(7, 1, "created"), r(3, 2, "in transit"), r(7, 3, "delivered"), r(3, 1, "created"), r(7, 2, "in transit"))) ==
            listOf(r(3, 2, "in transit"), r(7, 3, "delivered")),
    )
    check(latestVersions(listOf(r(5, 4, "first"), r(5, 4, "replayed"), r(1, 1, "only"))) == listOf(r(1, 1, "only"), r(5, 4, "first")))
    check(latestVersions(listOf()).isEmpty())
    check(latestVersions(listOf(r(42, 9, "x"))) == listOf(r(42, 9, "x")))
    check(
        latestVersions(listOf(r(10, 1, "a"), r(2, 1, "b"), r(10, 2, "c"), r(-4, 3, "d"), r(2, 5, "e"))) ==
            listOf(r(-4, 3, "d"), r(2, 5, "e"), r(10, 2, "c")),
    )
    println("192 ok")
}
