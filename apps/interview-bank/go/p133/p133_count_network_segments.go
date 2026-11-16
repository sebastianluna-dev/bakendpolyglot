// 133 · Count Network Segments
package p133

func CountSegments(n int, links [][]int) int {
	parent := make([]int, n)
	size := make([]int, n)
	for i := range parent {
		parent[i] = i
		size[i] = 1
	}
	segments := n
	for _, link := range links {
		ra, rb := find(parent, link[0]), find(parent, link[1])
		if ra == rb {
			continue
		}
		if size[ra] < size[rb] {
			ra, rb = rb, ra
		}
		parent[rb] = ra // the smaller tree goes under the larger one
		size[ra] += size[rb]
		segments--
	}
	return segments
}

func find(parent []int, x int) int {
	for parent[x] != x {
		parent[x] = parent[parent[x]] // path halving: point x at its grandparent
		x = parent[x]
	}
	return x
}
