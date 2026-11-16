// 140 · Cheapest Way to Connect All Sites
package p140

import "slices"

func CheapestConnection(n int, cables [][]int) int {
	sorted := slices.Clone(cables)
	slices.SortFunc(sorted, func(a, b []int) int { return a[2] - b[2] })
	parent := make([]int, n)
	size := make([]int, n)
	for i := range parent {
		parent[i] = i
		size[i] = 1
	}
	total, used := 0, 0
	for _, c := range sorted {
		ra, rb := find(parent, c[0]), find(parent, c[1])
		if ra == rb {
			continue // both ends already connected: this cable would close a loop
		}
		if size[ra] < size[rb] {
			ra, rb = rb, ra
		}
		parent[rb] = ra
		size[ra] += size[rb]
		total += c[2]
		used++
	}
	if used != n-1 {
		return -1
	}
	return total
}

func find(parent []int, x int) int {
	for parent[x] != x {
		parent[x] = parent[parent[x]] // path halving: point x at its grandparent
		x = parent[x]
	}
	return x
}
