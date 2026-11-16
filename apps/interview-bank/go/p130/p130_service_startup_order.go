// 130 · Service Startup Order
package p130

import "container/heap"

// minHeap implements heap.Interface for ints.
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

func StartupOrder(n int, dependencies [][]int) []int {
	dependents := make([][]int, n)
	waiting := make([]int, n) // dependencies not yet started
	for _, d := range dependencies {
		dependents[d[1]] = append(dependents[d[1]], d[0])
		waiting[d[0]]++
	}
	ready := &minHeap{}
	for s := range n {
		if waiting[s] == 0 {
			heap.Push(ready, s)
		}
	}
	order := []int{}
	for ready.Len() > 0 {
		service := heap.Pop(ready).(int)
		order = append(order, service)
		for _, d := range dependents[service] {
			waiting[d]--
			if waiting[d] == 0 {
				heap.Push(ready, d)
			}
		}
	}
	if len(order) != n {
		return []int{}
	}
	return order
}
