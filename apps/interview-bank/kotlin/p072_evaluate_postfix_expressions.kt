// 072 · Evaluate Postfix Expressions
package p072

fun evaluatePostfix(tokens: Array<String>): Int {
    val stack = ArrayDeque<Int>()
    for (token in tokens) {
        when (token) {
            "+", "-", "*", "/" -> {
                val b = stack.removeLast()
                val a = stack.removeLast()
                stack.addLast(
                    when (token) {
                        "+" -> a + b
                        "-" -> a - b
                        "*" -> a * b
                        else -> a / b
                    },
                )
            }
            else -> stack.addLast(token.toInt())
        }
    }
    return stack.last()
}

fun main() {
    check(evaluatePostfix(arrayOf("4", "13", "5", "/", "+")) == 6)
    check(evaluatePostfix(arrayOf("-7", "2", "/", "3", "*")) == -9)
    check(evaluatePostfix(arrayOf("42")) == 42)
    check(evaluatePostfix(arrayOf("8", "2", "/", "3", "-")) == 1)
    check(evaluatePostfix(arrayOf("2", "-5", "/")) == 0)
    check(evaluatePostfix(arrayOf("-9", "-2", "/")) == 4)
    check(evaluatePostfix(arrayOf("5", "1", "2", "+", "4", "*", "+", "3", "-")) == 14)
    println("072 ok")
}
