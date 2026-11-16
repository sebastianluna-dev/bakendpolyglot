// 145 · Schedule Tasks With a Cooldown
package p145

import "container/heap"

type maxHeap []int

func (h maxHeap) Len() int           { return len(h) }
func (h maxHeap) Less(i, j int) bool { return h[i] > h[j] } // copies left per type, most first
func (h maxHeap) Swap(i, j int)      { h[i], h[j] = h[j], h[i] }
func (h *maxHeap) Push(x any)        { *h = append(*h, x.(int)) }
func (h *maxHeap) Pop() any {
	old := *h
	x := old[len(old)-1]
	*h = old[:len(old)-1]
	return x
}

type cooling struct{ left, ready int } // ready: the time it may run again

func ScheduleTime(tasks string, cooldown int) int {
	var counts [26]int
	for _, t := range tasks {
		counts[t-'A']++
	}
	h := &maxHeap{}
	for _, c := range counts {
		if c > 0 {
			heap.Push(h, c)
		}
	}
	var waiting []cooling
	time := 0
	for h.Len() > 0 || len(waiting) > 0 {
		time++
		if h.Len() == 0 {
			time = waiting[0].ready // idle until the next type comes off cooldown
		} else {
			if left := heap.Pop(h).(int) - 1; left > 0 {
				waiting = append(waiting, cooling{left, time + cooldown})
			}
		}
		if len(waiting) > 0 && waiting[0].ready == time {
			heap.Push(h, waiting[0].left)
			waiting = waiting[1:]
		}
	}
	return time
}
