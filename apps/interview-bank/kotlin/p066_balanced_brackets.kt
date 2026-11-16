// 066 · Balanced Brackets
package p066

fun isBalanced(s: String): Boolean {
    val expected = ArrayDeque<Char>() // the closer each open bracket is waiting for
    for (ch in s) {
        when (ch) {
            '(' -> expected.addLast(')')
            '[' -> expected.addLast(']')
            '{' -> expected.addLast('}')
            ')', ']', '}' -> if (expected.removeLastOrNull() != ch) return false
        }
    }
    return expected.isEmpty()
}

fun main() {
    check(isBalanced("lookup(ids[0], {limit: 5})"))
    check(!isBalanced("([)]"))
    check(isBalanced(""))
    check(isBalanced("no brackets"))
    check(!isBalanced("("))
    check(!isBalanced(")"))
    check(!isBalanced("}{"))
    check(!isBalanced("(("))
    check(isBalanced("{[()()]}"))
    println("066 ok")
}
