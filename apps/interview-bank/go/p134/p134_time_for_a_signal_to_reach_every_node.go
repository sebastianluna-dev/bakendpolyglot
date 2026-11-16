// 134 · Time for a Signal to Reach Every Node
package p134

import (
	"container/heap"
	"math"
)

type entry struct{ dist, node int }

type minHeap []entry

func (h minHeap) Len() int           { return len(h) }
func (h minHeap) Less(i, j int) bool { return h[i].dist < h[j].dist }
func (h minHeap) Swap(i, j int)      { h[i], h[j] = h[j], h[i] }
func (h *minHeap) Push(x any)        { *h = append(*h, x.(entry)) }
func (h *minHeap) Pop() any {
	old := *h
	e := old[len(old)-1]
	*h = old[:len(old)-1]
	return e
}

func SignalTime(n int, links [][]int, source int) int {
	type edge struct{ to, ms int }
	graph := make([][]edge, n)
	for _, l := range links {
		graph[l[0]] = append(graph[l[0]], edge{l[1], l[2]})
	}
	dist := make([]int, n)
	for i := range dist {
		dist[i] = math.MaxInt
	}
	dist[source] = 0
	h := &minHeap{{0, source}}
	for h.Len() > 0 {
		top := heap.Pop(h).(entry) // heap.Pop, not h.Pop: the package function restores the order
		if top.dist > dist[top.node] {
			continue // stale: a shorter path was found after this was queued
		}
		for _, e := range graph[top.node] {
			if nd := top.dist + e.ms; nd < dist[e.to] {
				dist[e.to] = nd
				heap.Push(h, entry{nd, e.to})
			}
		}
	}
	slowest := 0
	for _, d := range dist {
		slowest = max(slowest, d)
	}
	if slowest == math.MaxInt {
		return -1
	}
	return slowest
}
