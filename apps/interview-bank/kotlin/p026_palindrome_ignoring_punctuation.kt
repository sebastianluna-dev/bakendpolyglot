// 026 · Palindrome, Ignoring Punctuation
package p026

fun isPalindrome(text: String): Boolean {
    var left = 0
    var right = text.length - 1
    while (left < right) {
        when {
            !text[left].isLetterOrDigit() -> left++
            !text[right].isLetterOrDigit() -> right--
            text[left].lowercaseChar() != text[right].lowercaseChar() -> return false
            else -> {
                left++
                right--
            }
        }
    }
    return true
}

fun main() {
    check(isPalindrome("Step on no pets!"))
    check(!isPalindrome("Pets on no steps"))
    check(isPalindrome(""))
    check(isPalindrome(",.! ?"))
    check(isPalindrome("a."))
    check(isPalindrome("Ab1bA"))
    check(!isPalindrome("1a2"))
    check(!isPalindrome("0P"))
    println("026 ok")
}
