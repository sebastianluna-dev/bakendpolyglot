// 046 · Is One a Subsequence of the Other?
package p046

func IsSubsequence(plan, scans string) bool {
	i, j := 0, 0
	for i < len(plan) && j < len(scans) {
		if plan[i] == scans[j] {
			i++
		}
		j++
	}
	return i == len(plan)
}
