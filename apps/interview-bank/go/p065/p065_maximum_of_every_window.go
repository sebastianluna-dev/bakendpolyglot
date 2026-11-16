// 065 · Maximum of Every Window
package p065

func WindowMaxima(readings []int, k int) []int {
	result := []int{}
	window := []int{} // indices; their readings decrease from window[0] to the back
	for i, r := range readings {
		for len(window) > 0 && readings[window[len(window)-1]] <= r {
			window = window[:len(window)-1]
		}
		window = append(window, i)
		if window[0] <= i-k {
			window = window[1:]
		}
		if i >= k-1 {
			result = append(result, readings[window[0]])
		}
	}
	return result
}
