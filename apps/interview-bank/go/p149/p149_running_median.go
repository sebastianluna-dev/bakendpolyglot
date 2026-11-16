// 149 · Running Median
package p149

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

type RunningMedian struct {
	low  *IntHeap // the smaller half, negated so that its largest value is on top
	high *IntHeap // the larger half, smallest on top
}

func NewRunningMedian() *RunningMedian {
	return &RunningMedian{low: &IntHeap{}, high: &IntHeap{}}
}

func (r *RunningMedian) AddNum(num int) {
	heap.Push(r.low, -num)
	heap.Push(r.high, -heap.Pop(r.low).(int))
	if r.high.Len() > r.low.Len() {
		heap.Push(r.low, -heap.Pop(r.high).(int))
	}
}

func (r *RunningMedian) FindMedian() float64 {
	if r.low.Len() > r.high.Len() {
		return float64(-(*r.low)[0])
	}
	return float64(-(*r.low)[0]+(*r.high)[0]) / 2
}
