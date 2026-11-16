// 064 · Rain Caught Between Walls
package p064

func TrappedRain(heights []int) int {
	left, right := 0, len(heights)-1
	leftMax, rightMax, water := 0, 0, 0
	for left < right {
		// The lower end decides: the far side has a wall at least this high.
		if heights[left] < heights[right] {
			leftMax = max(leftMax, heights[left])
			water += leftMax - heights[left]
			left++
		} else {
			rightMax = max(rightMax, heights[right])
			water += rightMax - heights[right]
			right--
		}
	}
	return water
}
