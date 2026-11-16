// 143 · Medal Ranks
package p143

import (
	"container/heap"
	"strconv"
)

type entry struct{ score, index int }

type maxHeap []entry

func (h maxHeap) Len() int           { return len(h) }
func (h maxHeap) Less(i, j int) bool { return h[i].score > h[j].score } // highest score on top
func (h maxHeap) Swap(i, j int)      { h[i], h[j] = h[j], h[i] }
func (h *maxHeap) Push(x any)        { *h = append(*h, x.(entry)) }
func (h *maxHeap) Pop() any {
	old := *h
	x := old[len(old)-1]
	*h = old[:len(old)-1]
	return x
}

func MedalRanks(scores []int) []string {
	h := &maxHeap{}
	for i, s := range scores {
		heap.Push(h, entry{s, i})
	}
	medals := []string{"Gold Medal", "Silver Medal", "Bronze Medal"}
	ranks := make([]string, len(scores))
	for place := 1; h.Len() > 0; place++ {
		e := heap.Pop(h).(entry)
		if place <= 3 {
			ranks[e.index] = medals[place-1]
		} else {
			ranks[e.index] = strconv.Itoa(place)
		}
	}
	return ranks
}
