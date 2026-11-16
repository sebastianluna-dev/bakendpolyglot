// 148 · How Far With Bricks and Ladders
package p148

import "container/heap"

type IntHeap []int

func (h IntHeap) Len() int           { return len(h) }
func (h IntHeap) Less(i, j int) bool { return h[i] < h[j] }
func (h IntHeap) Swap(i, j int)      { h[i], h[j] = h[j], h[i] }
func (h *IntHeap) Push(x any)        { *h = append(*h, x.(int)) }
func (h *IntHeap) Pop() any {
	old := *h
	x := old[len(old)-1]
	*h = old[:len(old)-1]
	return x
}

func FurthestPlatform(heights []int, bricks, ladders int) int {
	onLadders := &IntHeap{} // the climbs ladders cover so far
	for i := 0; i < len(heights)-1; i++ {
		climb := heights[i+1] - heights[i]
		if climb <= 0 {
			continue
		}
		heap.Push(onLadders, climb)
		if onLadders.Len() > ladders {
			bricks -= heap.Pop(onLadders).(int) // the smallest climb goes to bricks
		}
		if bricks < 0 {
			return i
		}
	}
	return len(heights) - 1
}
