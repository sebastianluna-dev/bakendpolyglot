// 015 · The k Most Borrowed Books
package p015

fun mostBorrowed(loans: List<String>, k: Int): List<String> {
    val counts = HashMap<String, Int>()
    for (title in loans) counts[title] = (counts[title] ?: 0) + 1
    return counts.keys
        .sortedWith(compareByDescending<String> { counts.getValue(it) }.thenBy { it })
        .take(k)
}

fun main() {
    val loans = listOf("dune", "emma", "dune", "ulysses", "emma", "dune", "beloved")
    check(mostBorrowed(loans, 2) == listOf("dune", "emma"))
    check(mostBorrowed(listOf("emma", "beloved", "ulysses", "beloved", "emma"), 2) == listOf("beloved", "emma"))
    check(mostBorrowed(listOf("dune"), 3) == listOf("dune"))
    check(mostBorrowed(listOf(), 1).isEmpty())
    check(mostBorrowed(listOf("c", "b", "a"), 2) == listOf("a", "b"))
    println("015 ok")
}
