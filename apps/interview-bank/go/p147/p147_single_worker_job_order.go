// 147 · Single-Worker Job Order
package p147

import (
	"container/heap"
	"sort"
)

type job struct{ duration, index int }

type jobHeap []job

func (h jobHeap) Len() int { return len(h) }
func (h jobHeap) Less(i, j int) bool { // shortest first, then the smaller index
	if h[i].duration != h[j].duration {
		return h[i].duration < h[j].duration
	}
	return h[i].index < h[j].index
}
func (h jobHeap) Swap(i, j int) { h[i], h[j] = h[j], h[i] }
func (h *jobHeap) Push(x any)   { *h = append(*h, x.(job)) }
func (h *jobHeap) Pop() any {
	old := *h
	x := old[len(old)-1]
	*h = old[:len(old)-1]
	return x
}

func JobOrder(jobs [][]int) []int {
	n := len(jobs)
	byArrival := make([]int, n)
	for i := range byArrival {
		byArrival[i] = i
	}
	sort.Slice(byArrival, func(a, b int) bool { return jobs[byArrival[a]][0] < jobs[byArrival[b]][0] })
	ready := &jobHeap{}
	order := make([]int, 0, n)
	time := 0
	next := 0 // position in byArrival of the first job not yet queued
	for len(order) < n {
		if ready.Len() == 0 && time < jobs[byArrival[next]][0] {
			time = jobs[byArrival[next]][0] // idle
		}
		for next < n && jobs[byArrival[next]][0] <= time {
			i := byArrival[next]
			heap.Push(ready, job{jobs[i][1], i})
			next++
		}
		j := heap.Pop(ready).(job)
		time += j.duration
		order = append(order, j.index)
	}
	return order
}
