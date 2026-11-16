// 142 · Smash the Heaviest Stones
package p142

import (
	"container/heap"
	"slices"
)

type maxHeap []int

func (h maxHeap) Len() int           { return len(h) }
func (h maxHeap) Less(i, j int) bool { return h[i] > h[j] } // > makes it a max-heap
func (h maxHeap) Swap(i, j int)      { h[i], h[j] = h[j], h[i] }
func (h *maxHeap) Push(x any)        { *h = append(*h, x.(int)) }
func (h *maxHeap) Pop() any {
	old := *h
	x := old[len(old)-1]
	*h = old[:len(old)-1]
	return x
}

func LastStone(stones []int) int {
	h := maxHeap(slices.Clone(stones))
	heap.Init(&h)
	for h.Len() > 1 {
		x := heap.Pop(&h).(int)
		y := heap.Pop(&h).(int)
		if x > y {
			heap.Push(&h, x-y)
		}
	}
	if h.Len() == 0 {
		return 0
	}
	return h[0]
}
