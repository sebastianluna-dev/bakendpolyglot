// 156 · Combinations That Sum to a Target
package p156

import "slices"

func CombinationsToTarget(candidates []int, target int) [][]int {
	sizes := slices.Clone(candidates)
	slices.Sort(sizes)
	result := [][]int{}
	path := []int{}
	var walk func(start, remaining int)
	walk = func(start, remaining int) {
		if remaining == 0 {
			result = append(result, slices.Clone(path))
			return
		}
		for i := start; i < len(sizes); i++ {
			if sizes[i] > remaining {
				break // sorted: every later size is too big as well
			}
			path = append(path, sizes[i])
			walk(i, remaining-sizes[i]) // i, not i + 1: the same size may be used again
			path = path[:len(path)-1]
		}
	}
	walk(0, target)
	return result
}
