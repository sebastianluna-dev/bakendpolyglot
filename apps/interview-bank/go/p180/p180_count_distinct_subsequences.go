// 180 · Count Distinct Subsequences
package p180

const mod = 1_000_000_007

func CountSubsequences(s, t string) int {
	m := len(t)
	ways := make([]int, m+1) // ways[j]: ways to spell the first j letters of t so far
	ways[0] = 1
	for i := 0; i < len(s); i++ {
		for j := m; j >= 1; j-- { // downwards, so this letter of s is used at most once
			if t[j-1] == s[i] {
				ways[j] = (ways[j] + ways[j-1]) % mod
			}
		}
	}
	return ways[m]
}
