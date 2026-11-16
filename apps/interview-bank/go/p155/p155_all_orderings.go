// 155 · All Orderings
package p155

import "slices"

func AllOrderings(nums []int) [][]int {
	result := [][]int{}
	path := []int{}
	used := make([]bool, len(nums))
	var walk func()
	walk = func() {
		if len(path) == len(nums) {
			result = append(result, slices.Clone(path))
			return
		}
		for i, x := range nums {
			if used[i] {
				continue
			}
			used[i] = true
			path = append(path, x)
			walk()
			path = path[:len(path)-1]
			used[i] = false
		}
	}
	walk()
	return result
}
