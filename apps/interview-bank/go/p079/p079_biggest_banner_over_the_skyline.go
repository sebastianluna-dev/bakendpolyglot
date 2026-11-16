// 079 · Biggest Banner Over the Skyline
package p079

func BiggestBanner(heights []int) int {
	best := 0
	stack := []int{} // indices of rising heights
	for i := 0; i <= len(heights); i++ {
		h := 0 // a zero at the end empties the stack
		if i < len(heights) {
			h = heights[i]
		}
		for len(stack) > 0 && heights[stack[len(stack)-1]] >= h {
			top := stack[len(stack)-1]
			stack = stack[:len(stack)-1]
			left := -1
			if len(stack) > 0 {
				left = stack[len(stack)-1]
			}
			best = max(best, heights[top]*(i-left-1))
		}
		stack = append(stack, i)
	}
	return best
}
