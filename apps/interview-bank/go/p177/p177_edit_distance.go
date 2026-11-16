// 177 · Edit Distance
package p177

func EditDistance(source, target string) int {
	m := len(target)
	// cur[j]: fewest edits turning the first i letters of source into the first j of target; prev holds row i - 1
	prev := make([]int, m+1)
	for j := range prev {
		prev[j] = j
	}
	for i := 1; i <= len(source); i++ {
		cur := make([]int, m+1)
		cur[0] = i
		for j := 1; j <= m; j++ {
			if source[i-1] == target[j-1] {
				cur[j] = prev[j-1]
			} else {
				cur[j] = 1 + min(prev[j], cur[j-1], prev[j-1]) // delete, insert, replace
			}
		}
		prev = cur
	}
	return prev[m]
}
