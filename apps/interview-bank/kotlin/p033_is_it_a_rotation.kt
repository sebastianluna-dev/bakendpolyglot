// 033 · Is It a Rotation?
package p033

fun isRotation(a: String, b: String): Boolean = a.length == b.length && b in a + a

fun main() {
    check(isRotation("parcel", "celpar"))
    check(!isRotation("parcel", "parcle"))
    check(isRotation("abc", "abc"))
    check(isRotation("", ""))
    check(!isRotation("aa", "a"))
    check(isRotation("abab", "baba"))
    println("033 ok")
}
