// 056 · Longest Run After k Replacements
package p056

import "slices"

func LongestRunAfterReplacements(s string, k int) int {
	var counts [26]int
	left, best := 0, 0
	for right := 0; right < len(s); right++ {
		counts[s[right]-'A']++
		// Letters to replace = window length minus the most common letter in it.
		for right-left+1-slices.Max(counts[:]) > k {
			counts[s[left]-'A']--
			left++
		}
		best = max(best, right-left+1)
	}
	return best
}
