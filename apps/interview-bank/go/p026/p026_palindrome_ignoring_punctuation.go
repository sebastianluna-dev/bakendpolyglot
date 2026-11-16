// 026 · Palindrome, Ignoring Punctuation
package p026

import "unicode"

func IsPalindrome(text string) bool {
	left, right := 0, len(text)-1
	for left < right {
		a, b := rune(text[left]), rune(text[right])
		switch {
		case !isLetterOrDigit(a):
			left++
		case !isLetterOrDigit(b):
			right--
		case unicode.ToLower(a) != unicode.ToLower(b):
			return false
		default:
			left++
			right--
		}
	}
	return true
}

func isLetterOrDigit(r rune) bool {
	return unicode.IsLetter(r) || unicode.IsDigit(r)
}
