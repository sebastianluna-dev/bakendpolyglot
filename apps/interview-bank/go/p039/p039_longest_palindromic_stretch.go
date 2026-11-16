// 039 · Longest Palindromic Stretch
package p039

func LongestPalindrome(s string) string {
	bestStart, bestLength := 0, 0
	for i := range len(s) {
		for _, length := range []int{expand(s, i, i), expand(s, i, i+1)} {
			if length > bestLength { // strictly longer, so the first stretch wins a tie
				bestLength = length
				bestStart = i - (length-1)/2
			}
		}
	}
	return s[bestStart : bestStart+bestLength]
}

func expand(s string, lo, hi int) int {
	for lo >= 0 && hi < len(s) && s[lo] == s[hi] {
		lo--
		hi++
	}
	return hi - lo - 1
}
