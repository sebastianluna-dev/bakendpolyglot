// 182 · Hand Out Cookies
package p182

import "slices"

func ContentChildren(greed, sizes []int) int {
	slices.Sort(greed)
	slices.Sort(sizes)
	child := 0 // the least greedy child still without a cookie
	for _, size := range sizes {
		if child < len(greed) && size >= greed[child] {
			child++
		}
	}
	return child
}
