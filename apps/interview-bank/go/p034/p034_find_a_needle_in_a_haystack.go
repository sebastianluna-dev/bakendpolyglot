// 034 · Find a Needle in a Haystack
package p034

func FindNeedle(haystack, needle string) int {
	for start := 0; start+len(needle) <= len(haystack); start++ {
		k := 0
		for k < len(needle) && haystack[start+k] == needle[k] {
			k++
		}
		if k == len(needle) {
			return start
		}
	}
	return -1
}
