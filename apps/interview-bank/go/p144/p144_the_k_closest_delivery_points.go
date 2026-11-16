// 144 · The k Closest Delivery Points
package p144

import "container/heap"

type point struct{ dist, x, y int }

// farther reports whether a sorts after b: by distance², then x, then y.
func farther(a, b point) bool {
	if a.dist != b.dist {
		return a.dist > b.dist
	}
	if a.x != b.x {
		return a.x > b.x
	}
	return a.y > b.y
}

type maxHeap []point

func (h maxHeap) Len() int           { return len(h) }
func (h maxHeap) Less(i, j int) bool { return farther(h[i], h[j]) } // farthest kept point on top
func (h maxHeap) Swap(i, j int)      { h[i], h[j] = h[j], h[i] }
func (h *maxHeap) Push(x any)        { *h = append(*h, x.(point)) }
func (h *maxHeap) Pop() any {
	old := *h
	x := old[len(old)-1]
	*h = old[:len(old)-1]
	return x
}

func KClosest(points [][]int, k int) [][]int {
	h := &maxHeap{}
	for _, p := range points {
		heap.Push(h, point{p[0]*p[0] + p[1]*p[1], p[0], p[1]})
		if h.Len() > k {
			heap.Pop(h)
		}
	}
	result := make([][]int, h.Len())
	for i := len(result) - 1; i >= 0; i-- {
		p := heap.Pop(h).(point)
		result[i] = []int{p.x, p.y}
	}
	return result
}
