// 157 · Every Balanced Bracket String
package p157

fun balancedBrackets(n: Int): List<String> {
    val result = mutableListOf<String>()
    val path = StringBuilder()
    fun walk(opened: Int, closed: Int) {
        if (closed == n) {
            result.add(path.toString())
            return
        }
        if (opened < n) {
            path.append('(')
            walk(opened + 1, closed)
            path.setLength(path.length - 1)
        }
        if (closed < opened) {
            path.append(')')
            walk(opened, closed + 1)
            path.setLength(path.length - 1)
        }
    }
    walk(0, 0)
    return result
}

fun main() {
    check(balancedBrackets(3) == listOf("((()))", "(()())", "(())()", "()(())", "()()()"))
    check(balancedBrackets(1) == listOf("()"))
    check(balancedBrackets(0) == listOf(""))
    check(balancedBrackets(2) == listOf("(())", "()()"))
    check(balancedBrackets(4).size == 14)
    println("157 ok")
}
