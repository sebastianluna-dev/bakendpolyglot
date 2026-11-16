// 058 · Shortest Stretch Reaching a Total
package p058

func ShortestReaching(weights []int, target int) int {
	n := len(weights)
	best := n + 1 // longer than any real stretch: means "none found"
	left, sum := 0, 0
	for right := 0; right < n; right++ {
		sum += weights[right]
		for sum >= target {
			best = min(best, right-left+1)
			sum -= weights[left]
			left++
		}
	}
	if best == n+1 {
		return 0
	}
	return best
}
