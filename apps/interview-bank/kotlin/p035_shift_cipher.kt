// 035 · Shift Cipher
package p035

fun shiftCipher(text: String, shift: Int): String {
    val k = (shift % 26 + 26) % 26 // % keeps the sign of shift, so -1 must become 25
    val chars = text.toCharArray()
    for (i in chars.indices) {
        val ch = chars[i]
        if (ch in 'a'..'z') chars[i] = 'a' + (ch - 'a' + k) % 26
        else if (ch in 'A'..'Z') chars[i] = 'A' + (ch - 'A' + k) % 26
    }
    return String(chars)
}

fun main() {
    check(shiftCipher("Hold seat 14!", 3) == "Krog vhdw 14!")
    check(shiftCipher("xyz", 2) == "zab")
    check(shiftCipher("Abc", -1) == "Zab")
    check(shiftCipher("abc", 52) == "abc")
    check(shiftCipher("Zz", -27) == "Yy")
    check(shiftCipher("Krog vhdw 14!", -3) == "Hold seat 14!")
    check(shiftCipher("", 5) == "")
    println("035 ok")
}
