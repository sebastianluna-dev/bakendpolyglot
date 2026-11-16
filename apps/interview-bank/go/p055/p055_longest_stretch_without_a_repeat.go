// 055 · Longest Stretch Without a Repeat
package p055

func LongestWithoutRepeat(s string) int {
	last := map[byte]int{} // character -> index where it was last seen
	start, best := 0, 0
	for i := 0; i < len(s); i++ {
		if prev, ok := last[s[i]]; ok && prev >= start {
			start = prev + 1 // only a repeat inside the window counts
		}
		last[s[i]] = i
		best = max(best, i-start+1)
	}
	return best
}
