// 178 · Pattern Matching With . and *
package p178

func MatchesPattern(text, pattern string) bool {
	m := len(pattern)
	// cur[j]: the first i letters of text match the first j characters of pattern; prev holds row i - 1
	prev := make([]bool, m+1)
	for i := 0; i <= len(text); i++ {
		cur := make([]bool, m+1)
		cur[0] = i == 0
		for j := 1; j <= m; j++ {
			if pattern[j-1] == '*' {
				// the starred item matches nothing, or it takes text[i-1] and may take more
				cur[j] = cur[j-2] || (i > 0 && fits(text[i-1], pattern[j-2]) && prev[j])
			} else {
				cur[j] = i > 0 && fits(text[i-1], pattern[j-1]) && prev[j-1]
			}
		}
		prev = cur
	}
	return prev[m]
}

func fits(c, p byte) bool {
	return p == '.' || p == c
}
