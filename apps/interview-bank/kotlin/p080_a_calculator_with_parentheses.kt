// 080 · A Calculator With Parentheses
package p080

fun calculate(s: String): Int {
    var result = 0
    var sign = 1
    var number = 0
    val stack = ArrayDeque<Int>() // for each open bracket: the result before it, then the sign in front of it
    for (ch in s) {
        when (ch) {
            in '0'..'9' -> number = number * 10 + (ch - '0')
            '+', '-' -> {
                result += sign * number
                number = 0
                sign = if (ch == '+') 1 else -1
            }
            '(' -> {
                stack.addLast(result)
                stack.addLast(sign)
                result = 0
                sign = 1
            }
            ')' -> {
                result += sign * number
                number = 0
                val outerSign = stack.removeLast()
                result = stack.removeLast() + outerSign * result
            }
        }
    }
    return result + sign * number
}

fun main() {
    check(calculate("7 - (3 - (1 + 2))") == 7)
    check(calculate("-(4 - (-2 + 9)) + 10") == 13)
    check(calculate("12") == 12)
    check(calculate(" 1 + 2 ") == 3)
    check(calculate("-5") == -5)
    check(calculate("(((3)))") == 3)
    check(calculate("10 - 2 - 3") == 5)
    check(calculate("1 - (2 + 3)") == -4)
    check(calculate("-(-(-1))") == -1)
    check(calculate("2147483647") == 2147483647)
    println("080 ok")
}
