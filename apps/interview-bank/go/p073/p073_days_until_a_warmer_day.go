// 073 · Days Until a Warmer Day
package p073

func DaysUntilWarmer(temps []int) []int {
	wait := make([]int, len(temps))
	stack := []int{} // indices still waiting, their temperatures never rising from bottom to top
	for i, t := range temps {
		for len(stack) > 0 && temps[stack[len(stack)-1]] < t {
			j := stack[len(stack)-1]
			stack = stack[:len(stack)-1]
			wait[j] = i - j
		}
		stack = append(stack, i)
	}
	return wait
}
