// 154 · All Subsets
package p154

import "slices"

func AllSubsets(nums []int) [][]int {
	result := [][]int{}
	path := []int{}
	var walk func(start int)
	walk = func(start int) {
		result = append(result, slices.Clone(path)) // a copy: path's array is about to be reused
		for i := start; i < len(nums); i++ {
			path = append(path, nums[i])
			walk(i + 1)
			path = path[:len(path)-1]
		}
	}
	walk(0)
	return result
}
