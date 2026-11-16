// 190 · Hand Out Candy Fairly
package p190

func MinCandy(scores []int) int {
	n := len(scores)
	candy := make([]int, n)
	for i := range candy {
		candy[i] = 1
	}
	for i := 1; i < n; i++ {
		if scores[i] > scores[i-1] {
			candy[i] = candy[i-1] + 1
		}
	}
	for i := n - 2; i >= 0; i-- {
		// max keeps what the first pass gave for the left neighbour
		if scores[i] > scores[i+1] {
			candy[i] = max(candy[i], candy[i+1]+1)
		}
	}
	total := 0
	for _, c := range candy {
		total += c
	}
	return total
}
