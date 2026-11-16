// 044 · Fully Justify Text
package p044

fun justifyText(words: List<String>, width: Int): List<String> {
    val lines = mutableListOf<String>()
    var i = 0
    while (i < words.size) {
        var j = i + 1
        var used = words[i].length // the words plus one space between each pair
        while (j < words.size && used + 1 + words[j].length <= width) {
            used += 1 + words[j].length
            j++
        }
        val gaps = j - i - 1
        if (j == words.size || gaps == 0) {
            lines.add(words.subList(i, j).joinToString(" ").padEnd(width))
        } else {
            val spaces = width - (used - gaps)
            val each = spaces / gaps
            val extra = spaces % gaps
            lines.add(buildString {
                for (k in i until j - 1) {
                    append(words[k]).append(" ".repeat(each + if (k - i < extra) 1 else 0))
                }
                append(words[j - 1])
            })
        }
        i = j
    }
    return lines
}

fun main() {
    check(
        justifyText(listOf("Your", "loan", "of", "Dune", "is", "due", "on", "Friday."), 16) ==
            listOf("Your   loan   of", "Dune  is  due on", "Friday.         "),
    )
    check(
        justifyText(listOf("Hold", "expires", "in", "ten", "minutes"), 10) ==
            listOf("Hold      ", "expires in", "ten       ", "minutes   "),
    )
    check(justifyText(listOf("to", "be", "or", "not"), 6) == listOf("to  be", "or not"))
    check(justifyText(listOf("exact"), 5) == listOf("exact"))
    check(justifyText(listOf(), 8).isEmpty())
    println("044 ok")
}
