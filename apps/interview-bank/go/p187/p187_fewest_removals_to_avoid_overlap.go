// 187 · Fewest Removals to Avoid Overlap
package p187

import (
	"cmp"
	"math"
	"slices"
)

func FewestRemovals(intervals [][]int) int {
	byEnd := slices.Clone(intervals)
	slices.SortFunc(byEnd, func(a, b []int) int { return cmp.Compare(a[1], b[1]) })
	removals := 0
	lastEnd := math.MinInt // end of the last interval kept
	for _, iv := range byEnd {
		if iv[0] >= lastEnd {
			lastEnd = iv[1]
		} else {
			removals++
		}
	}
	return removals
}
