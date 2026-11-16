// 005 · The Majority Genre
package p005

fun majorityGenre(loans: Array<String>): String {
    val counts = HashMap<String, Int>()
    for (genre in loans) {
        val c = (counts[genre] ?: 0) + 1
        counts[genre] = c
        if (c * 2 > loans.size) return genre
    }
    return ""
}

fun main() {
    check(majorityGenre(arrayOf("mystery", "poetry", "mystery", "history", "mystery")) == "mystery")
    check(majorityGenre(arrayOf("poetry", "history", "poetry", "history")) == "")
    check(majorityGenre(arrayOf("drama", "comedy", "drama")) == "drama")
    check(majorityGenre(arrayOf("sci-fi")) == "sci-fi")
    check(majorityGenre(arrayOf()) == "")
    println("005 ok")
}
