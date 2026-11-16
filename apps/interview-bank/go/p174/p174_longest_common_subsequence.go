// 174 · Longest Common Subsequence
package p174

func LongestCommonSubsequence(a, b string) int {
	m := len(b)
	// cur[j]: answer for the first i letters of a and the first j of b; prev holds row i - 1
	prev := make([]int, m+1)
	for i := 1; i <= len(a); i++ {
		cur := make([]int, m+1)
		for j := 1; j <= m; j++ {
			if a[i-1] == b[j-1] {
				cur[j] = prev[j-1] + 1
			} else {
				cur[j] = max(prev[j], cur[j-1])
			}
		}
		prev = cur
	}
	return prev[m]
}
