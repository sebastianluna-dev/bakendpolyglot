// 045 · Shortest Palindrome by Prepending
package p045

import "slices"

func ShortestPalindrome(s string) string {
	rev := []byte(s)
	slices.Reverse(rev)
	reversed := string(rev)
	t := s + "#" + reversed // the '#' stops a match from running past the end of s
	fail := make([]int, len(t))
	k := 0
	for i := 1; i < len(t); i++ {
		for k > 0 && t[i] != t[k] {
			k = fail[k-1]
		}
		if t[i] == t[k] {
			k++
		}
		fail[i] = k
	}
	keep := fail[len(t)-1] // length of the longest palindromic prefix of s
	return reversed[:len(s)-keep] + s
}
