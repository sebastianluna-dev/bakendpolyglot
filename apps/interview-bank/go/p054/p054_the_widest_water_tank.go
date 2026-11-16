// 054 · The Widest Water Tank
package p054

func WidestTank(heights []int) int {
	left, right, best := 0, len(heights)-1, 0
	for left < right {
		area := (right - left) * min(heights[left], heights[right])
		best = max(best, area)
		// The shorter wall limits every tank that keeps it, so move it inward.
		if heights[left] <= heights[right] {
			left++
		} else {
			right--
		}
	}
	return best
}
