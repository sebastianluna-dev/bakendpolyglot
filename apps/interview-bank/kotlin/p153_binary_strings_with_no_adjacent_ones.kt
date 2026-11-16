// 153 · Binary Strings With No Adjacent Ones
package p153

fun noAdjacentOnes(n: Int): List<String> {
    val result = mutableListOf<String>()
    val path = StringBuilder()
    fun walk() {
        if (path.length == n) {
            result.add(path.toString())
            return
        }
        path.append('0')
        walk()
        path.setLength(path.length - 1)
        if (path.isEmpty() || path.last() == '0') {
            path.append('1')
            walk()
            path.setLength(path.length - 1)
        }
    }
    walk()
    return result
}

fun main() {
    check(noAdjacentOnes(3) == listOf("000", "001", "010", "100", "101"))
    check(noAdjacentOnes(1) == listOf("0", "1"))
    check(noAdjacentOnes(0) == listOf(""))
    check(noAdjacentOnes(2) == listOf("00", "01", "10"))
    check(noAdjacentOnes(4) == listOf("0000", "0001", "0010", "0100", "0101", "1000", "1001", "1010"))
    check(noAdjacentOnes(10).size == 144)
    println("153 ok")
}
