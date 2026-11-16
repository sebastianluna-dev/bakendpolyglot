// 185 · Merge Overlapping Intervals
package p185

import (
	"cmp"
	"slices"
)

func MergeIntervals(intervals [][]int) [][]int {
	sorted := slices.Clone(intervals)
	slices.SortFunc(sorted, func(a, b []int) int { return cmp.Compare(a[0], b[0]) })
	merged := [][]int{}
	for _, iv := range sorted {
		if n := len(merged); n > 0 && iv[0] <= merged[n-1][1] {
			merged[n-1][1] = max(merged[n-1][1], iv[1])
		} else {
			merged = append(merged, []int{iv[0], iv[1]}) // a new pair, so extending it never changes the input
		}
	}
	return merged
}
