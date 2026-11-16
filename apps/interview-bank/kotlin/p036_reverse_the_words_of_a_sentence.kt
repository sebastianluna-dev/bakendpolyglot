// 036 · Reverse the Words of a Sentence
package p036

fun reverseWords(sentence: String): String =
    sentence.split(" ").filter { it.isNotEmpty() }.reversed().joinToString(" ")

fun main() {
    check(reverseWords("seat 14 is free") == "free is 14 seat")
    check(reverseWords("  return   the copy ") == "copy the return")
    check(reverseWords("hold") == "hold")
    check(reverseWords("") == "")
    check(reverseWords("   ") == "")
    check(reverseWords("a b c") == "c b a")
    println("036 ok")
}
