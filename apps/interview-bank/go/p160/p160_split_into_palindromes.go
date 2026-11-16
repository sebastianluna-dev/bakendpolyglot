// 160 · Split Into Palindromes
package p160

import "slices"

func PalindromeSplits(s string) [][]string {
	result := [][]string{}
	path := []string{}
	isPalindrome := func(lo, hi int) bool {
		for lo < hi {
			if s[lo] != s[hi] {
				return false
			}
			lo++
			hi--
		}
		return true
	}
	var walk func(start int)
	walk = func(start int) {
		if start == len(s) {
			result = append(result, slices.Clone(path))
			return
		}
		for end := start; end < len(s); end++ {
			if !isPalindrome(start, end) {
				continue
			}
			path = append(path, s[start:end+1])
			walk(end + 1)
			path = path[:len(path)-1]
		}
	}
	walk(0)
	return result
}
