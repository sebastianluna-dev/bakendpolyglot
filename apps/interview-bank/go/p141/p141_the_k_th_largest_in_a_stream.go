// 141 · The k-th Largest in a Stream
package p141

import "container/heap"

type minHeap []int

func (h minHeap) Len() int           { return len(h) }
func (h minHeap) Less(i, j int) bool { return h[i] < h[j] }
func (h minHeap) Swap(i, j int)      { h[i], h[j] = h[j], h[i] }
func (h *minHeap) Push(x any)        { *h = append(*h, x.(int)) }
func (h *minHeap) Pop() any {
	old := *h
	x := old[len(old)-1]
	*h = old[:len(old)-1]
	return x
}

type KthLargest struct {
	k   int
	top minHeap // the k largest values so far; the smallest is at index 0
}

func NewKthLargest(k int, nums []int) *KthLargest {
	s := &KthLargest{k: k}
	for _, x := range nums {
		s.Add(x)
	}
	return s
}

func (s *KthLargest) Add(value int) int {
	heap.Push(&s.top, value)
	if s.top.Len() > s.k {
		heap.Pop(&s.top) // drop the smallest: it is no longer in the top k
	}
	return s.top[0]
}
