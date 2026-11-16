// 125 · Find the Trusted Judge
package p125

func FindJudge(n int, trust [][]int) int {
	score := make([]int, n+1) // trusted by, minus trusts
	for _, t := range trust {
		score[t[0]]--
		score[t[1]]++
	}
	for person := 1; person <= n; person++ {
		if score[person] == n-1 {
			return person
		}
	}
	return -1
}
