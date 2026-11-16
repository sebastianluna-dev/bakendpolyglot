// 181 · Can One Person Attend Every Meeting?
package p181

import (
	"cmp"
	"slices"
)

func CanAttendAll(meetings [][]int) bool {
	slices.SortFunc(meetings, func(a, b []int) int { return cmp.Compare(a[0], b[0]) })
	for i := 1; i < len(meetings); i++ {
		if meetings[i][0] < meetings[i-1][1] { // starting just as the last one ends is fine
			return false
		}
	}
	return true
}
